package com.intelliteq.fea.ammocalculator.modifyCalculation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import java.lang.IllegalArgumentException

class ModifyCalculationViewModelFactory(
    private val calculationKey: Long,
    private val days: Int,
    private val intensity: String,
    private val name: String,
    private val calculation: CalculationDao
) : ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModifyCalculationViewModel::class.java)) {
            return ModifyCalculationViewModel(calculationKey, days, intensity, name, calculation) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}