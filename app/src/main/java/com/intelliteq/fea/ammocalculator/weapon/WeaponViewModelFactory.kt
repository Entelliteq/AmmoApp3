package com.intelliteq.fea.ammocalculator.weapon

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import java.lang.IllegalArgumentException

/**
 * Class to create a new view model for Weapon
 */
class WeaponViewModelFactory (
    private val dataSource: WeaponDao,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeaponViewModel::class.java)) {
            return WeaponViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}