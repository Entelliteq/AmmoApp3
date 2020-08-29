package com.intelliteq.fea.ammocalculator.calculate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import java.lang.IllegalArgumentException

class CalculateViewModelFactory  (
    private val database: WeaponDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculateViewModel::class.java)) {
            return CalculateViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}