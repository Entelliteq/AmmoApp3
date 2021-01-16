package com.intelliteq.fea.ammocalculator.deleteWeapon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.editWeaponInput.EditWeaponViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import java.lang.IllegalArgumentException

class DeleteWeaponViewModelFactory (
    private val weaponKey: Long,
    private val componentDao: ComponentDao,
    private val ammoDao: AmmoDao,
    private val calculationDao: CalculationDao,
    private val perWeaponCalculationDao: PerWeaponCalculationDao
) : ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteWeaponViewModel::class.java)) {
            return DeleteWeaponViewModel(
                weaponKey, componentDao, ammoDao, calculationDao, perWeaponCalculationDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}