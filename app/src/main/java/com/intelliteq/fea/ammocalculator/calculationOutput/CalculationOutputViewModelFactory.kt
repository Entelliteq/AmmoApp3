package com.intelliteq.fea.ammocalculator.calculationOutput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao

import java.lang.IllegalArgumentException

class CalculationOutputViewModelFactory(
    private val calculationKey: Long,
    private val calculation: CalculationDao,
    private val perWeapon: PerWeaponCalculationDao,
    val days: Int,
    private val intensity: String) : ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculationOutputViewModel::class.java)) {
            return CalculationOutputViewModel( calculationKey,  calculation, perWeapon, days, intensity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}