package com.intelliteq.fea.ammocalculator.componentAmmo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentAmmoDao

class ComponentAmmoViewModel (
    val database: ComponentAmmoDao,
    application: Application
) : AndroidViewModel(application){

}