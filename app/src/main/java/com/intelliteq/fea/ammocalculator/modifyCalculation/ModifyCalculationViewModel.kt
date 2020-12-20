package com.intelliteq.fea.ammocalculator.modifyCalculation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
import kotlinx.coroutines.*

class ModifyCalculationViewModel(
    val calculationKey: Long,
    val days: Int,
    val intensity: String,
    val name: String,
    var calculation: CalculationDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var assaultIntensityString = MutableLiveData<String>()

    private val _numberOfDaysPicker = MutableLiveData<Int>()
    private val numberOfDaysPicker: LiveData<Int>
        get() = _numberOfDaysPicker

    private var calculationFromDB = Calculation()

    val daysChangedValue = MutableLiveData<Int>()
    val intensityChangedValue = MutableLiveData<String>()


   // val weapons = calculation.getSelectedWeapons(calculationKey)
    val weapons = calculation.getSelectedWeaponsForCalculationOutput(calculationKey)

    private var daysChanged = false
    private var intensityChanged = false

    init {
        getCalculationItem()
    }

    fun assaultIntensityStringToIntValues(combat: String) {
        if(combat != "none") {
            assaultIntensityString.value = combat
            Log.i("Days11", "TO INT ${assaultIntensityString.value}")
            intensityChanged = true
        }

    }


    fun getHowManyDays(number: Int) {
        _numberOfDaysPicker.value = number
        daysChanged = true
    }


    fun onSave() {
        if (daysChanged) {
            updateDaysChanged()
            daysChangedValue.value = numberOfDaysPicker.value!!
        }
        if (intensityChanged) {
            updateIntensityChanged()
            intensityChangedValue.value = assaultIntensityString.value!!
        }
    }


    private fun getCalculationItem() {
        uiScope.launch {
            calculationFromDB = getCalcFromDatabaseSuspend()
            daysChangedValue.value = days
            intensityChangedValue.value = intensity
            Log.i("final", "$calculationFromDB")
        }
    }

    private suspend fun getCalcFromDatabaseSuspend(): Calculation {
        return withContext(Dispatchers.IO) {
            val thiscalc = calculation.get(calculationKey)
            thiscalc
        }
    }

    private fun updateDaysChanged() {
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



    private fun updateIntensityChanged() {
        uiScope.launch {
            Log.i("days22", " updateDaysChanged()")
            calculationFromDB.assaultIntensity = assaultIntensityString.value!!
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