package com.intelliteq.fea.ammocalculator.calculationOutput

import android.util.Log
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import kotlinx.coroutines.*

class CalculationOutputViewModel(
    val calculationKey: Long,
    var calculation: CalculationDao,
    val perWeapon: PerWeaponCalculationDao,
    val days: Int,
    val intensity: String

) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val weapon = calculation.getSelectedWeapons(calculationKey)
    val perWeaponCalcUsed = perWeapon.getUsingCalculationID(calculationKey)
    val calculationUsed = calculation.getLive(calculationKey)

    val ammos = calculation.getSelectedAmmos(calculationKey)


//    init {
//       Log.i("out1 init", "from int///////// ${ammos.value}")
//
//    }


    fun test() {
        val uniqueAmmos = listOf<Ammo>()
        Log.i("sort b4", "from init2///////// ${ammos.value}")
        ammos.value?.sortedBy { it.ammoDODIC }
        Log.i("sort after", "from init2///////// ${ammos.value}")
    }
}

