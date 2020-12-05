package com.intelliteq.fea.ammocalculator.savedCalculations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
import kotlinx.coroutines.Job

class SavedCalculationsViewModel(
    calculation: CalculationDao

) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()

    val calculations = calculation.getAllSavedCalculations()

    private val _navigateToModifyCalculation = MutableLiveData<Calculation>()
    val naviagteToModifyCalculation
        get() = _navigateToModifyCalculation

    fun onCalculationClicked(id: Calculation) {
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