package com.intelliteq.fea.ammocalculator.calculationOutput

import android.app.Application
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.SingleWeaponCalculationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CalculationOutputViewModel(
    val singleWeaponCalculationDatabase: SingleWeaponCalculationDao,
    val application: Application
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)



}