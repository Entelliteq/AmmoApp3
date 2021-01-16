package com.intelliteq.fea.ammocalculator.calculationOutput

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.formulas.BasicAmmoFormula
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.PerWeaponCalculation
import kotlinx.coroutines.*


class CalculationOutputViewModel(
    val calculationKey: Long,
    var calculation: CalculationDao,
    private var perWeapon: PerWeaponCalculationDao,
    val days: Int,
    val intensity: String

) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    val weapon = calculation.getSelectedWeaponsForCalculationOutput(calculationKey)
    val perWeaponCalcUsed = perWeapon.getUsingCalculationID(calculationKey)

    val ammo = calculation.getSelectedAmmos(calculationKey)
    var calculationName = MutableLiveData<String>()
    val name: LiveData<String>
        get() = calculationName

    private var perWeaponCalculationsNeeded = listOf<PerWeaponCalculation>()

    //use for intensity and number of days
    private val calculationUsedThis = MutableLiveData<Calculation>()

    //ammo cards
    private val ammoCardsList = mutableListOf<AmmoCalculationCardData>()
    val ammoCards = MutableLiveData<List<AmmoCalculationCardData>>() //sent to adapter

    //weapon cards
    private val weaponCardsList = mutableListOf<WeaponCardData>()
    val weaponCards = MutableLiveData<List<WeaponCardData>>() //sent to adapter
    var weaponThis = MutableLiveData<Component>()

    lateinit var weaponReturned: Component

    private var ammoUsed = listOf<Ammo>()
    private var ammoAggregated = listOf<Ammo>()

    val shareTextValue = MutableLiveData<String>()

    init {
        calculateResults()
    }


    /**
     * Calls to create the cards to send to adapter and create part of the text string
     */
    private fun calculateResults() {
        uiScope.launch {
            calculationUsedThis.value = calculationFromDatabase()
            perWeaponCalculationsNeeded = perCalculationFromDatabase()

            ammoUsed = ammoFromDatabase()
            ammoAggregated = ammoAggregatedFromDatabase()

            shareTextValue.value = "\nOutput\n\tCalculation\nNumber of Days: $days\tOperation Intensity: $intensity"
            var i = 0
            shareTextValue.value +="\n\nWeapon --"
            for(w in weapon.value!!){
                shareTextValue.value += "\nType: ${w.componentTypeId} \tFEA: ${w.FEA_id} \tQuantity: ${perWeaponCalculationsNeeded[i++].numberOfWeapons}"
                shareTextValue.value += "\nDescription: ${w.componentDescription} "
            }
            shareTextValue.value += "\n\nAmmunition --"

            createAmmoCardWithMap(createAmmoMap(ammoAggregated))
            createWeaponCard()
        }

    }



    private fun createWeaponCard() {
        uiScope.launch {

            for (p in perWeaponCalcUsed.value!!){
                val comp = componentUsedForCalculation(p.weaponIDCalculation)
                Log.i("card12", "$comp")
                val weaponCard = WeaponCardData(comp.componentTypeId, comp.FEA_id, p.numberOfWeapons, comp.componentDescription)
                weaponCardsList.add(weaponCard)
                Log.i("adapt2 weapon" ,"${weaponCard}")
            }

            weaponCards.value = weaponCardsList
            for (w in weaponCardsList) {
                Log.i("adapt3" , "$w")
            }

        }
    }

    private suspend fun componentUsedForCalculation(id: Long) : Component {
        return withContext(Dispatchers.IO) {
            val comp = calculation.getSelectedWeapon(calculationKey, id)
            comp
        }
    }
    /**
     * PerMap creates PerWeaponCalculation Mapped to Ammo ID
     */
    private fun createPerMap(per: List<PerWeaponCalculation>): Map<Long, PerWeaponCalculation> {
        val perMap = mutableMapOf<Long, PerWeaponCalculation>()
        for (p in per) {
            perMap[p.weaponAmmoIdCalculation] = p
            perMap[p.componentAmmoIdCalculation] = p
        }
        return perMap
    }


    /**
     * Creates the card and continues to create text string
     */
    private fun createAmmoCardWithMap(
        ammo: Map<Ammo, Int>
    ) {
        for (a in ammo) {
            for (p in perWeaponCalculationsNeeded) {
                if (p.weaponAmmoIdCalculation == a.key.ammoAutoId) {
                    val tempPer = intensityStringToValue(
                        calculationUsedThis.value!!.assaultIntensity, a.key
                    ) * calculationUsedThis.value!!.numberOfDays
                    val tempTotal = totalAmmoCalc(a.key, calculationUsedThis.value!!, a.value)
                    //create card
                    val cardForWeapon = AmmoCalculationCardData(
                        a.key.ammoDescription!!,
                        a.key.ammoDODIC!!,
                        tempPer,
                        tempTotal
                    )
                    shareTextValue.value += "\nAmmo desc: ${a.key.ammoDescription}"
                    shareTextValue.value += "\nDODIC: ${a.key.ammoDODIC} \tPer Weapon: $tempPer \tTotal: $tempTotal"
                    ammoCardsList.add(cardForWeapon)

                } else if (p.componentAmmoIdCalculation == a.key.ammoAutoId) {
                    val tempPer = intensityStringToValue(
                        calculationUsedThis.value!!.assaultIntensity, a.key
                    ) * calculationUsedThis.value!!.numberOfDays
                    val tempTotal = totalAmmoCalc(
                        a.key,
                        calculationUsedThis.value!!,
                        a.value
                    )
                    //create card
                    val cardForAmmo = AmmoCalculationCardData(
                        a.key.ammoDescription!!,
                        a.key.ammoDODIC!!,
                        tempPer,
                        tempTotal
                    )
                    shareTextValue.value += "\nAmmo desc: ${a.key.ammoDescription}"
                    shareTextValue.value += "\nDODIC: ${a.key.ammoDODIC} \tPer Weapon: $tempPer \tTotal: $tempTotal"
                    ammoCardsList.add(cardForAmmo)
                }
            }
        }
        ammoCards.value = ammoCardsList
    }

    private fun getWeaponCount(ammo: Ammo): Int {
        var count = 0
        val map = createPerMap(perWeaponCalculationsNeeded)
        for (m in map) {
            if (ammo.ammoAutoId == m.key) {
                if (ammo.weaponId == m.value.weaponIDCalculation) {
                    count = m.value.numberOfWeapons
                    break
                }
            }
        }
        return count
    }

    /**
     * Map the Ammo to the weapon
     */
    private fun createAmmoMap(ammo: List<Ammo>): Map<Ammo, Int> {
        val ammoMap = mutableMapOf<Ammo, Int>()
        if (ammo.isEmpty()) return ammoMap
        val size = ammo.size
        var temp = ammo[0] //first ammo in the list
        var count: Int
        var tempCount = getWeaponCount(temp)
        ammoMap[temp] = tempCount  //first add to map at temp
        for (i in 1 until size) {
            if (temp.ammoDODIC == ammo[i].ammoDODIC) {
                tempCount = getWeaponCount(ammo[i])
                count = ammoMap.getValue(temp) + tempCount
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ammoMap.remove(temp)
                    ammoMap[temp] = count
                }  //update map

            } else {
                temp = ammo[i]
                tempCount = getWeaponCount(ammo[i])
                ammoMap[temp] = tempCount
            }
        }
        return ammoMap
    }

    //Suspend functions
    private suspend fun ammoFromDatabase(): List<Ammo> {
        return withContext(Dispatchers.IO) {
            val list = calculation.getSelectedAmmosList(calculationKey)
            list
        }
    }

    private suspend fun ammoAggregatedFromDatabase(): List<Ammo> {
        return withContext(Dispatchers.IO) {
            val list = calculation.getSelectedAggregatedAmmosList(calculationKey)
            list
        }
    }

    private suspend fun perCalculationFromDatabase(): List<PerWeaponCalculation> {
        return withContext(Dispatchers.IO) {
            val list = perWeapon.getUsingCalculationIDList(calculationKey)
            list
        }
    }

    private suspend fun calculationFromDatabase(): Calculation {
        return withContext(Dispatchers.IO) {
            val calc = calculation.get(calculationKey)
            calc
        }
    }

    private suspend fun updateDatabase() {
        withContext(Dispatchers.IO) {
            calculation.update(calculationUsedThis.value!!)
            // calculation.update(calculationUsed.value!!)
        }
    }
    //return total ammunition
    private fun totalAmmoCalc(item: Ammo, calcItem: Calculation, quantity: Int): Int {
        val total: Int
        val assault = calcItem.assaultIntensity
        val calculation = BasicAmmoFormula(
            intensityStringToValue(assault, item),
            calcItem.numberOfDays,
            quantity
        )
        total = calculation.calculate()
        return total
    }


    private fun intensityStringToValue(combat: String, item: Ammo): Int {
        return when (combat) {
            "Training" -> item.trainingRate
            "Security" -> item.securityRate
            "Sustain" -> item.sustainRate
            "Light Assault" -> item.lightAssaultRate
            "Medium Assault" -> item.mediumAssaultRate
            else -> item.heavyAssaultRate
        }

    }

    fun saveNameFromDialog(name: String, text: String) {
        saveName(name, text)
    }

    private fun saveName(nameEntered: String, text: String) {
        uiScope.launch {
                calculationUsedThis.value?.calculationName = nameEntered
                calculationUsedThis.value?.textString = text
                updateDatabase()
        }
    }

    /**
     * Cancelling all jobs
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}

