package com.intelliteq.fea.ammocalculator.savedCalculations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import java.lang.IllegalArgumentException

class SavedCalculationsViewModelFactory(
    private val calculation: CalculationDao

) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedCalculationsViewModel::class.java)) {
            return SavedCalculationsViewModel(
                calculation
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}