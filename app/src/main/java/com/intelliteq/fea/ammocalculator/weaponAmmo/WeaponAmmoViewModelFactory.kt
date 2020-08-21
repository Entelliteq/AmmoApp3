package com.intelliteq.fea.ammocalculator.weaponAmmo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponAmmoDao
import java.lang.IllegalArgumentException

/**
 * Class to create a new view model for WeaponAmmo
 */
class WeaponAmmoViewModelFactory (
    private val weaponAmmoKey: Long = 0L,
    private val database: WeaponAmmoDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeaponAmmoViewModel::class.java)) {
            return WeaponAmmoViewModel(weaponAmmoKey, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}