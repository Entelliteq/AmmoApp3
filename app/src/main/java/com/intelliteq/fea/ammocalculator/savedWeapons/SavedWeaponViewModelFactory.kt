package com.intelliteq.fea.ammocalculator.savedWeapons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.savedCalculations.SavedCalculationsViewModel

class SavedWeaponViewModelFactory (
    private val weapons: ComponentDao

) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedWeaponsViewModel::class.java)) {
            return SavedWeaponsViewModel(
                weapons
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}