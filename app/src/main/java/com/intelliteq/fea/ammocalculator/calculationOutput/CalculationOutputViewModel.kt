package com.intelliteq.fea.ammocalculator.calculationOutput

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.formulas.BasicAmmoFormula
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
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

    private val calculationCard = mutableListOf<CalculationCardData>()
    val cards = MutableLiveData<List<CalculationCardData>>()
    private var ammoUsed = listOf<Ammo>()
    private var ammoAggregated = listOf<Ammo>()

    val shareTextValue = MutableLiveData<String>()

    init {
        calculateResults()

    }


    private fun calculateResults() {
        uiScope.launch {
            calculationUsedThis.value = calculationFromDatabase()
            perWeaponCalculationsNeeded = perCalculationFromDatabase()

            ammoUsed = ammoFromDatabase()
            ammoAggregated = ammoAggregatedFromDatabase()
            for (a in ammoAggregated) {
                Log.i("err3", "aggreg: ${a}")
            }
            shareTextValue.value = "\nOutput\n\tCalculation\nNumber of Days: $days\tOperation Intensity: $intensity"
            var i = 0
            shareTextValue.value +="\n\nWeapon --"
            for(w in weapon.value!!){
                shareTextValue.value += "\nType: ${w.componentTypeId} \tFEA: ${w.FEA_id} \tQuantity: ${perWeaponCalculationsNeeded.get(i++).numberOfWeapons}"
                shareTextValue.value += "\nDescription: ${w.componentDescription} "
            }
            shareTextValue.value += "\n\nAmmunition --"

            createCardWithMap(
                createAmmoMap(ammoAggregated),
                createPerMap(perWeaponCalculationsNeeded)
            )

        }

    }

    fun addTextToDatabase(text: String) {
        Log.i("text3", "from fun: ${text}")
    }

    private fun createPerMap(per: List<PerWeaponCalculation>): Map<Long, PerWeaponCalculation> {
        Log.i("err4", "////////////////////////////////////////////")
        var perMap = mutableMapOf<Long, PerWeaponCalculation>()

        // Log.i("err4", "${per} ")
        for (p in per) {
            perMap.put(p.weaponAmmoIdCalculation, p)
            perMap.put(p.componentAmmoIdCalculation, p)
        }

        for (a in perMap) {
            Log.i("err4", "${a.key} ;; ${a.value}")
        }

        return perMap

    }


    private fun createCardWithMap(
        ammo: Map<Ammo, Int>,
        perWeapon: Map<Long, PerWeaponCalculation>
    ) {
        for (a in ammo) {
            for (p in perWeaponCalculationsNeeded) {
                if (p.weaponAmmoIdCalculation == a.key.ammoAutoId) {
                    val tempPer = intensityStringToValue(
                        calculationUsedThis.value!!.assaultIntensity, a.key
                    ) *
                            calculationUsedThis.value!!.numberOfDays
                    val tempTotal = totalAmmoCalc(a.key, calculationUsedThis.value!!, a.value)
                    val weaponCard = CalculationCardData(
                        a.key.ammoDescription!!,
                        a.key.ammoDODIC!!,
                        tempPer,
                        tempTotal
                    )
                    shareTextValue.value += "\nAmmo desc: ${a.key.ammoDescription}"
                    shareTextValue.value += "\nDODIC: ${a.key.ammoDODIC} \tPer Weapon: $tempPer \tTotal: $tempTotal"

                    calculationCard.add(weaponCard)
                } else if (p.componentAmmoIdCalculation == a.key.ammoAutoId) {
                    val tempPer = intensityStringToValue(
                        calculationUsedThis.value!!.assaultIntensity, a.key
                    ) *
                            calculationUsedThis.value!!.numberOfDays
                    val tempTotal = totalAmmoCalc(
                        a.key,
                        calculationUsedThis.value!!,
                        a.value
                    ) //  * ammo[a.key]!!
                    //create card
                    val compCard = CalculationCardData(
                        a.key.ammoDescription!!,
                        a.key.ammoDODIC!!,
                        tempPer,
                        tempTotal
                    )
                    shareTextValue.value += "\nAmmo desc: ${a.key.ammoDescription}"
                    shareTextValue.value += "\nDODIC: ${a.key.ammoDODIC} \tPer Weapon: $tempPer \tTotal: $tempTotal"
                    calculationCard.add(compCard)
                }
            }
        }


        for (a in ammo) {
            Log.i("err5", "ammo: ${a.key.ammoAutoId} ;; ${a.key.ammoDODIC} ;; #weap: ${a.value}")
          //  Log.i("err5", "PER: ${perWeapon.get(a.key.ammoAutoId)}")
        }


//        for (p in perWeapon) {
//            Log.i("err5", "per: ${p.key} ;; #weap: ${p.value.numberOfWeapons}")
//        }







        cards.value = calculationCard
        for (c in calculationCard) {
            Log.i("err3", "card: ${c.ammoDODIC} ")
        }
    }

    private fun getWeaponCount(ammo: Ammo): Int {
        var count = 0
       Log.i("err6", "getCount for ${ammo} //////////////////////////")
        Log.i("err6", "getCount for ${perWeaponCalculationsNeeded} //////////////////////////")
        val map = createPerMap(perWeaponCalculationsNeeded)
        Log.i("err6", "Size of map: ${map.size}")
        for (m in map) {
            //  Log.i("err6", "a-dodic:${ammo.ammoDODIC} a-id:${ammo.ammoAutoId} == m-key:${m.key}  && a-wpnID:${ammo.weaponId} m-weaponID:${m.value.weaponIDCalculation} map-value:${m.value.numberOfWeapons}")
            if (ammo.ammoAutoId == m.key) {
                Log.i("err6", "ID MATCH <WD: ${ammo.weaponId}> <Map Weapon: ${m.value.weaponIDCalculation}>")
                if (ammo.weaponId == m.value.weaponIDCalculation) {
                    count = m.value.numberOfWeapons
                    //  Log.i("err6", "FOUND:: A-dodic:${ammo.ammoDODIC}  a-id:${ammo.ammoAutoId} m-weaponID:${m.value.weaponIDCalculation} map-value:${m.value.numberOfWeapons}")
                    Log.i(
                        "err6",
                        "***** FOUND: ${ammo.ammoDODIC} at weapon: ${m.value.weaponIDCalculation}"
                    )
                    break;
                }
            }
        }

//        for (p in perWeaponCalculationsNeeded) {
//            if (p.weaponAmmoIdCalculation == ammo.ammoAutoId) {
//                count = p.numberOfWeapons
//            } else if (p.componentAmmoIdCalculation == ammo.ammoAutoId) {
//                count = p.numberOfWeapons
//            }
//        }
        Log.i("err6", "return count: $count////////////////////")
        return count
    }

    private fun createAmmoMap(ammo: List<Ammo>): Map<Ammo, Int> {
        Log.i("err6", "createAmmoMap")
        val ammoMap = mutableMapOf<Ammo, Int>()
        for (a in ammo) {
            Log.i("err3", "${a.ammoDODIC} #weapon: ${getWeaponCount(a)}")
        }

        if (ammo.isEmpty()) return ammoMap
        val size = ammo.size
        var temp = ammo[0] //first ammo in the list
        var count: Int
        var tempCount = getWeaponCount(temp)
        Log.i("err5", " First count: $tempCount for ${temp.ammoDODIC}")
        ammoMap[temp] = tempCount  //first add to map at temp
        for (i in 1 until size) {
            if (temp.ammoDODIC == ammo[i].ammoDODIC) {
                tempCount = getWeaponCount(ammo[i])
                Log.i("err5", " TEMPCount: $tempCount")
                count = ammoMap.getValue(temp) + tempCount
                Log.i("err5", " getCOUNt: ${ammoMap.getValue(temp)} for ${ammoMap[temp]}")
                Log.i("err5", " COUNT TOTAL UPDATE: $count")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ammoMap.remove(temp)
                    ammoMap[temp] = count
                }  //update map

            } else {
                temp = ammo[i]
                tempCount = getWeaponCount(ammo[i])
                Log.i("err5", " ELSE TEMPCount: $tempCount for ${temp.ammoDODIC}")
                ammoMap[temp] = tempCount
            }
        }

        for (a in ammoMap) {
            Log.i("err3", "${a.key.ammoDODIC} V=${a.value}")
        }
        return ammoMap
    }


    private suspend fun ammoFromDatabase(): List<Ammo> {
        return withContext(Dispatchers.IO) {
            val list = calculation.getSelectedAmmosList(calculationKey)
            list
        }
    }


    private suspend fun ammoAggregatedFromDatabase(): List<Ammo> {
        return withContext(Dispatchers.IO) {
            val list = calculation.getSelectedAggregatedAmmosList(calculationKey)
            Log.i("err7", "list: $list")
            Log.i("err7", "per: $perWeaponCalculationsNeeded")
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
        Log.i("text3", "HELLO")
        uiScope.launch {

   //         if(calculationUsedThis.value?.calculationName  == "") {
                calculationUsedThis.value?.calculationName = nameEntered
                calculationUsedThis.value?.textString = text
                Log.i("text3", "calc used: ${calculationUsedThis.value}")
                updateDatabase()
//            } else {
//                Log.i("text3", "create new version")
//            }

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

