package com.intelliteq.fea.ammocalculator.savedCalculations

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SavedCalculationsViewModel(
    private val calculation: CalculationDao

) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val calculations = calculation.getAllSavedCalculations()

    var testCalc = Calculation()

    private val _navigateToModifyCalculation = MutableLiveData<Calculation>()
    val naviagteToModifyCalculation
        get() = _navigateToModifyCalculation

    fun onCalculationClicked(id: Calculation) {

//        testCalc = calculation.getOneCalc(id.calculationId).value!!
 //       Log.i("days12", "calc in saved $testCalc")
        _navigateToModifyCalculation.value = id
    }

    fun onModifyCalculationNavigated() {
        _navigateToModifyCalculation.value = null
    }


    /**
     * Cancelling all jobs
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}