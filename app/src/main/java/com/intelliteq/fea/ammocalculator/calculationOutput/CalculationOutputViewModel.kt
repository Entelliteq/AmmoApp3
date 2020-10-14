package com.intelliteq.fea.ammocalculator.calculationOutput

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationsDao
import com.intelliteq.fea.ammocalculator.persistence.daos.SingleWeaponCalculationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CalculationOutputViewModel(
    val calculationKey: Long,
    var calculations: CalculationsDao,
    val singleWeapon: SingleWeaponCalculationDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val weapon = calculations.getSelectedWeapons(calculationKey)
    val single = singleWeapon.getUsingCalculationID(calculationKey)



    val oneCalc = calculations.getOneCalc(calculationKey)



    init {

    }




}