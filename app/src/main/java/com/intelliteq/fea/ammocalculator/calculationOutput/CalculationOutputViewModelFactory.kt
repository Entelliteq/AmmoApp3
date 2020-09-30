package com.intelliteq.fea.ammocalculator.calculationOutput

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationsDao
import com.intelliteq.fea.ammocalculator.persistence.daos.SingleWeaponCalculationDao

import java.lang.IllegalArgumentException

class CalculationOutputViewModelFactory(
    private val calculationKey: Long,
    private val calculations: CalculationsDao,
    private val singleWeapon: SingleWeaponCalculationDao ) : ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculationOutputViewModel::class.java)) {
            return CalculationOutputViewModel( calculationKey,  calculations, singleWeapon) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}