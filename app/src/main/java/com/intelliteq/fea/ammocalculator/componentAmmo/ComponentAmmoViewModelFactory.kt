package com.intelliteq.fea.ammocalculator.componentAmmo


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import java.lang.IllegalArgumentException

/**
 * Class to create a new view model for ComponentAmmo
 */
class ComponentAmmoViewModelFactory (
    private val componentKey: Long = 0L,
    private val weaponKey: Long = 0L,
    private val database: AmmoDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComponentAmmoViewModel::class.java)) {
            return ComponentAmmoViewModel(componentKey, weaponKey, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}