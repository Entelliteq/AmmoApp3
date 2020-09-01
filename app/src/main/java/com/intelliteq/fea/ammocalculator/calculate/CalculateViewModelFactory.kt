package com.intelliteq.fea.ammocalculator.calculate

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import java.lang.IllegalArgumentException

class CalculateViewModelFactory  (
    private val database: WeaponDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculateViewModel::class.java)) {
            return CalculateViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}