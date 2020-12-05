package com.intelliteq.fea.ammocalculator.calculate


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.*
import java.lang.IllegalArgumentException

class CalculateViewModelFactory(
    private val calculationKey: Long,
    private val ammoDatabase: AmmoDao,
    private val componentDatabase: ComponentDao,
    private val perWeaponCalculationDatabase: PerWeaponCalculationDao,
    private val calculationDatabase: CalculationDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculateViewModel::class.java)) {
            return CalculateViewModel(
                calculationKey, ammoDatabase, componentDatabase, perWeaponCalculationDatabase,
                calculationDatabase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}