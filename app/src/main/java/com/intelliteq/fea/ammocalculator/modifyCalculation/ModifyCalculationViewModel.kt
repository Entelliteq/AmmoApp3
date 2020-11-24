package com.intelliteq.fea.ammocalculator.modifyCalculation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import kotlinx.coroutines.*

class ModifyCalculationViewModel(
    val calculationKey: Long,
    val days: Int,
    val intensity: String,
    val name: String,
    var calculation: CalculationDao,
    val perWeapon: PerWeaponCalculationDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var assaultIntensityString = MutableLiveData<String>()

    private val _numberOfDaysPicker = MutableLiveData<Int>()
    val numberOfDaysPicker: LiveData<Int>
        get() = _numberOfDaysPicker

    var calculationUsed = calculation.getThisCalculation(calculationKey)

    var calculationFromDB = Calculation()

    val nameCalc = name;
    //  val perWeaponCalc = perWeapon.getUsingCalculationID(calculationKey)

    val weapons = calculation.getSelectedWeapons(calculationKey)
  //  val perCalc = calculation.getSelectedWeaponCount(calculationKey)
    var daysChanged = false
    var intensityChanged = false

    init {
//        Log.i("days11", "init key: $calculationKey")
//        Log.i("days11", "init calc: ${calculationUsed.value}")
//       // Log.i("days11", "init weapon: ${perWeaponCalc.value}")
        getCalculationItem()
    }

    fun assaultIntensityStringToIntValues(combat: String) {
        if(combat != "none") {
            assaultIntensityString.value = combat
            Log.i("Days11", "TO INT ${assaultIntensityString.value}")
            intensityChanged = true
        }

    }

//    fun days(): String {
//        var thisCalc  = Calculation()
//        thisCalc = getOriginalDays() as Calculation
//        return thisCalc.numberOfDays.toString()
//    }
//
//    fun getOriginalDays(): Unit {
//        uiScope.launch {
//            var calc = Calculation()
//            calc = getOriginalFromDatabse()
//            return@launch calc as Unit//.numberOfDays.toString()
//        }
//    }
//
//
//    private suspend fun getOriginalFromDatabse(): Calculation {
//        return withContext(Dispatchers.IO) {
//            val thiscalc = calculation.get(calculationKey)
//            thiscalc
//        }
//    }

    fun getHowManyDays(number: Int) {
        _numberOfDaysPicker.value = number
        daysChanged = true
    }


    fun onSave() {
        if (daysChanged) {
            updateDaysChanged()
        }
        if (intensityChanged) {
            updateIntensityChanged()
        }
    }


    fun getCalculationItem() {
        uiScope.launch {
            calculationFromDB = getCalcFromDatabaseSuspend()
            Log.i("final", "$calculationFromDB")
        }
    }

    private suspend fun getCalcFromDatabaseSuspend(): Calculation {
        return withContext(Dispatchers.IO) {
            val thiscalc = calculation.get(calculationKey)
            thiscalc
        }
    }

    fun updateDaysChanged() {
        uiScope.launch {
            Log.i("days22", " updateDaysChanged()")
            calculationFromDB.numberOfDays = numberOfDaysPicker.value!!
            updateDatabase()
        }
    }


    private suspend fun updateDatabase() {
        withContext(Dispatchers.IO) {
            calculation.update(calculationFromDB)
        }
    }

    fun weaponChange() {
        Log.i("adapt", "test")
    }


    fun updateIntensityChanged() {
        uiScope.launch {
            Log.i("days22", " updateDaysChanged()")
            calculationFromDB.assaultIntensity = assaultIntensityString.value!!
            updateDatabase()

        }
    }

    private suspend fun updateCalculationForIntensity() {

    }

    /**
     * Cancelling all jobs
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}