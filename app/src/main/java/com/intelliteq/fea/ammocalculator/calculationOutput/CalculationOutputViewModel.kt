package com.intelliteq.fea.ammocalculator .calculationOutput

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
import kotlinx.coroutines.*

class CalculationOutputViewModel(
    val calculationKey: Long,
    var calculation: CalculationDao,
    val perWeapon: PerWeaponCalculationDao,
    val days: Int,
    val intensity: String

) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val weapon = calculation.getSelectedWeapons(calculationKey)
    val perWeaponCalcUsed = perWeapon.getUsingCalculationID(calculationKey)
    val calculationUsed = calculation.getThisCalculation(calculationKey)

    val ammos = calculation.getSelectedAmmos(calculationKey)
    var _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name



    fun saveNameFromDialog(saveThis: String) {
        saveName(saveThis)
    }


    fun saveName( nameEntered: String) {
        uiScope.launch {

            calculationUsed.value?.calculationName  = nameEntered
            Log.i("days13", "calc used: ${calculationUsed.value}")
            updateDatabase()

        }
    }

    private suspend fun updateDatabase(){
        withContext(Dispatchers.IO) {
            calculation.update(calculationUsed.value!!)
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

