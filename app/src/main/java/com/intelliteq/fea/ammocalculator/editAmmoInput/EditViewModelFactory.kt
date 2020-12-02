package com.intelliteq.fea.ammocalculator.editAmmoInput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.editComponentInput.EditComponentViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import java.lang.IllegalArgumentException

class EditAmmoViewModelFactory (
    private val componentKey: Long, //need componentKey
    private val ammoDao: AmmoDao
) : ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditAmmoViewModel::class.java)) {
            return EditAmmoViewModel( componentKey, ammoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}