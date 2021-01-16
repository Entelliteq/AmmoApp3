package com.intelliteq.fea.ammocalculator.deleteWeapon

import android.util.Log
import androidx.core.view.KeyEventDispatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import kotlinx.coroutines.*

class DeleteWeaponViewModel (
    private val weaponKey: Long,
    private val componentDatabase: ComponentDao,
    private val ammoDatabase: AmmoDao,
    private val calculationDatabase: CalculationDao,
    private val perWeaponCalculationDatabase: PerWeaponCalculationDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var weapon = MutableLiveData<Component?>()
    val ammosWeapon = ammoDatabase.getAllWeaponAmmos(weaponKey)
    val ammosComp = ammoDatabase.getAllComponentAmmos(weaponKey)
    val components = componentDatabase.getAllComponents(weaponKey)
    val calculation = calculationDatabase.getAllCalculations(weaponKey)


    init {
        getWeapon()
    }

    fun getWeapon() {
        uiScope.launch {
             weapon.value = getWeaponFromDatabase()
        }
    }

    private suspend fun getWeaponFromDatabase() : Component? {
        return withContext(Dispatchers.IO) {
            val  weep = componentDatabase.getWeapon(weaponKey)
            weep
        }
    }

    fun deleteCalculation(calc: Calculation) {
        uiScope.launch {
            deleteCalculationFromDatabase(calc)
        }
    }

    private suspend fun deleteCalculationFromDatabase(calc: Calculation) {
        withContext(Dispatchers.IO) {
            calculationDatabase.delete(calc)
        }
    }

    fun deleteComponent(comp: Component) {
        uiScope.launch {
            deleteComponentFromDatabase(comp)
        }
    }

    private suspend fun deleteComponentFromDatabase(comp: Component) {
        withContext(Dispatchers.IO) {
            componentDatabase.delete(comp)
        }
    }

    fun deleteAmmo(ammo: Ammo) {
        uiScope.launch {
            deleteAmmoFromDatabase(ammo)
        }
    }

    private suspend fun deleteAmmoFromDatabase(ammo: Ammo) {
        withContext(Dispatchers.IO) {
            ammoDatabase.delete(ammo)
        }
    }

}
