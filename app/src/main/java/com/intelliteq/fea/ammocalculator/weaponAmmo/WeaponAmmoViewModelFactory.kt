package com.intelliteq.fea.ammocalculator.weaponAmmo


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponAmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.weapon.WeaponViewModel
import java.lang.IllegalArgumentException

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