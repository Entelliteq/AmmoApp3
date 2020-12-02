package com.intelliteq.fea.ammocalculator.editWeaponInput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import java.lang.IllegalArgumentException

class EditWeaponViewModelFactory(
    private val weaponKey: Long,
    private val componentDao: ComponentDao
) : ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditWeaponViewModel::class.java)) {
            return EditWeaponViewModel( weaponKey, componentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}