package com.intelliteq.fea.ammocalculator.calculate

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.*
import java.lang.IllegalArgumentException

class CalculateViewModelFactory  (
    private val calculationKey: Long,
    private val weaponDatabase: WeaponDao,
    private val ammoDatabase: WeaponAmmoDao,
    private val componentDatabase: ComponentDao,
    private val componentAmmoDatabase: ComponentAmmoDao,
    private val singleWeaponCalculationDatabase: SingleWeaponCalculationDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculateViewModel::class.java)) {
            return CalculateViewModel(calculationKey, weaponDatabase, ammoDatabase, componentDatabase,
                componentAmmoDatabase, singleWeaponCalculationDatabase, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}