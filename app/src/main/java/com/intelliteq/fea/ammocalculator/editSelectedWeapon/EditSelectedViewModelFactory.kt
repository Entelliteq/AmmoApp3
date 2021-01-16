package com.intelliteq.fea.ammocalculator.editSelectedWeapon

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import java.lang.IllegalArgumentException

class EditSelectedViewModelFactory (
    private val ammoDatabase: AmmoDao,
    private val componentDatabase: ComponentDao,
    private val weaponKey: Long
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditSelectedViewModel::class.java)) {
            return EditSelectedViewModel(ammoDatabase, componentDatabase, weaponKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}