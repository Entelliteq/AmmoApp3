package com.intelliteq.fea.ammocalculator.calculationOutput

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.formulas.BasicAmmoFormula
import com.intelliteq.fea.ammocalculator.formulas.BasicPerWeaponFormula
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.*
import kotlinx.coroutines.*

class CalculationOutputViewModel(
    val calculationKey: Long,
    var calculation: CalculationDao,
    var perWeapon: PerWeaponCalculationDao,
    val days: Int,
    val intensity: String

) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val weapon = calculation.getSelectedWeapons(calculationKey)
    val perWeaponCalcUsed = perWeapon.getUsingCalculationID(calculationKey)
    val calculationUsed = calculation.getThisCalculation(calculationKey)

    val ammo = calculation.getSelectedAmmos(calculationKey)
    var calculationName = MutableLiveData<String>()
    val name: LiveData<String>
        get() = calculationName

    val weaponsUsed = MutableLiveData<Component>()
    val componentsUsed = MutableLiveData<Component>()
    var perWeaponCalculationsNeeded = listOf<PerWeaponCalculation>()

    //use for intensity and number of days
    val calculationUsedThis = MutableLiveData<Calculation>()
  //  val calculationCard = mutableListOf<CalculationCardData>()
    val calculationCard = mutableListOf<CalculationCardData>()
    val cards = MutableLiveData<List<CalculationCardData>>()
    var ammoUsed = listOf<Ammo>()

    init {
        calculateResults()

    }


    fun calculateResults() {
        uiScope.launch {
            calculationUsedThis.value = calculationFromDatabase()
            perWeaponCalculationsNeeded = perCalculationFromDatabase()
            ammoUsed = ammoFromDatabase()
            createCard(ammoUsed)

        }

    }

    fun createCard(ammo: List<Ammo>) {
        for(p in perWeaponCalculationsNeeded) {
            Log.i("Box11", "per: $p")
        }

        for (a in ammo) {
            Log.i("box11", "ammo $a")
            for (p in perWeaponCalculationsNeeded) {
//                if(p.weaponIDCalculation == a.weaponId) {
                    if (p.weaponAmmoIdCalculation == a.ammoAutoId) {
                        //create card
                        val weaponCard = CalculationCardData(
                            a.ammoDescription!!,
                            a.ammoDODIC!!,
                            perWeapon(a, calculationUsedThis.value!!, p),
                            totalAmmoCalc(a, calculationUsedThis.value!!, p)
                        )

                        calculationCard.add(weaponCard)
                    } else if (p.componentAmmoIdCalculation == a.ammoAutoId) {
                        //create card
                        val compCard = CalculationCardData(
                            a.ammoDescription!!,
                            a.ammoDODIC!!,
                            perWeapon(a, calculationUsedThis.value!!, p),
                            totalAmmoCalc(a, calculationUsedThis.value!!, p)
                        )
                        //calculationCard.value =compCard
                        calculationCard.add(compCard)
                    }
                }
//            }
//
        }

        cards.value = calculationCard
        Log.i("box11", "${cards.value}")
        //Log.i("box11", "size: ${calculationCard.size}")

    }

    private suspend fun ammoFromDatabase(): List<Ammo> {
        return withContext(Dispatchers.IO) {
            val list = calculation.getSelectedAmmosList(calculationKey)
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

//    private suspend fun weaponFromDatabase(): Component {
//        withContext(Dispatchers.IO) {
//            val weapon = calculation.getSelectedWeapons()
//        }
//    }

    fun totalAmmoCalc(item: Ammo, calcItem: Calculation, quantity: PerWeaponCalculation): Int {
        var total = 0
        var assault = calcItem.assaultIntensity
        val calculation = BasicAmmoFormula(
            intensityStringToValue(assault, item),
            calcItem.numberOfDays,
            quantity.numberOfWeapons
        )
        total = calculation.calculate()
        val temp = intensityStringToValue(assault, item)
        Log.i("box8", "ammo: ${item.ammoDODIC}")
        Log.i("box8", "count ${quantity.numberOfWeapons}")
        return total
    }

    fun perWeapon(item: Ammo, calcItem: Calculation, quantity: PerWeaponCalculation): Int {
        var per = 0
        val perCalc =
            BasicPerWeaponFormula(totalAmmoCalc(item, calcItem, quantity), quantity.numberOfWeapons)
        per = perCalc.calculate()


        return per
    }

    fun intensityStringToValue(combat: String, item: Ammo): Int {
        return when (combat) {
            "Training" -> item.trainingRate
            "Security" -> item.securityRate
            "Sustain" -> item.sustainRate
            "Light Assault" -> item.lightAssaultRate
            "Medium Assault" -> item.mediumAssaultRate
            else -> item.heavyAssaultRate
        }

    }


    fun saveNameFromDialog(saveThis: String) {
        saveName(saveThis)
    }

    private fun saveName(nameEntered: String) {
        uiScope.launch {

            calculationUsedThis.value?.calculationName = nameEntered
            Log.i("days13", "calc used: ${calculationUsedThis.value}")
            updateDatabase()

        }
    }

    private suspend fun updateDatabase() {
        withContext(Dispatchers.IO) {
            calculation.update(calculationUsedThis.value!!)
           // calculation.update(calculationUsed.value!!)
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

