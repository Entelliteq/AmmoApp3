package com.intelliteq.fea.ammocalculator.calculationOutput

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.SingleWeaponCalculationDao
import java.lang.IllegalArgumentException

class CalculationOutputViewModelFactory(
    private val singleWeaponCalculationDatabase: SingleWeaponCalculationDao,
    private val application: Application) : ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculationOutputViewModel::class.java)) {
            return CalculationOutputViewModel( singleWeaponCalculationDatabase, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}