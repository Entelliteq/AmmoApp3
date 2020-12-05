package com.intelliteq.fea.ammocalculator .calculationOutput

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import kotlinx.coroutines.*

class CalculationOutputViewModel(
    val calculationKey: Long,
    var calculation: CalculationDao,
    perWeapon: PerWeaponCalculationDao,
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



    fun saveNameFromDialog(saveThis: String) {
        saveName(saveThis)
    }


    private fun saveName(nameEntered: String) {
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

