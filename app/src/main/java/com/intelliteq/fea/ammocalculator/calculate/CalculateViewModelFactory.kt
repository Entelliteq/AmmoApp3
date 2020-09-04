package com.intelliteq.fea.ammocalculator.calculate

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentAmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponAmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import java.lang.IllegalArgumentException

class CalculateViewModelFactory  (
    private val weaponDatabase: WeaponDao,
    private val ammoDatabase: WeaponAmmoDao,
    private val componentDatabase: ComponentDao,
    private val componentAmmoDatabase: ComponentAmmoDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculateViewModel::class.java)) {
            return CalculateViewModel(weaponDatabase, ammoDatabase, componentDatabase, componentAmmoDatabase, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}