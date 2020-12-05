package com.intelliteq.fea.ammocalculator.validation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao

class ValidationViewModelFactory(
    private val weaponKey: Long,
    private val componentDatabase: ComponentDao,
    private val ammoDatabase: AmmoDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ValidationViewModel::class.java)) {
            return ValidationViewModel(weaponKey, componentDatabase, ammoDatabase ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}