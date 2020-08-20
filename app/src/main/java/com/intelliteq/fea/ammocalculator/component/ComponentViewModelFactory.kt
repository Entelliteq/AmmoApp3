package com.intelliteq.fea.ammocalculator.component

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import java.lang.IllegalArgumentException

class ComponentViewModelFactory (
    private val weaponKey: Long = 0L,
    private val database: ComponentDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComponentViewModel::class.java)) {
            return ComponentViewModel(weaponKey, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}