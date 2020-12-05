package com.intelliteq.fea.ammocalculator.editComponentInput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao

class EditComponentViewModelFactory (
    private val componentKey: Long, //need componentKey
    private val componentDao: ComponentDao
) : ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditComponentViewModel::class.java)) {
            return EditComponentViewModel( componentKey, componentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}