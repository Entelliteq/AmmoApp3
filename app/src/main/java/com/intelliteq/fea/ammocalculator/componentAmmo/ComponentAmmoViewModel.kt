package com.intelliteq.fea.ammocalculator.componentAmmo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentAmmoDao

class ComponentAmmoViewModel (
    private val componentKey: Long = 0L,
    private val database: ComponentAmmoDao
) : ViewModel() {

}