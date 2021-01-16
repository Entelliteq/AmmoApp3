package com.intelliteq.fea.ammocalculator.editSelectedWeapon

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import kotlinx.coroutines.*

class EditSelectedViewModel (
    private val ammoDatabase: AmmoDao,
    private val componentDatabase: ComponentDao,
    private val weaponKey: Long
) : ViewModel() {

    //create job and scope of coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    //create weapon to watch
    var weapon = MutableLiveData<Component?>()
    val ammosWeapon = ammoDatabase.getAllWeaponAmmos(weaponKey)
    val ammosComp = ammoDatabase.getAllComponentAmmos(weaponKey)
    val components = componentDatabase.getAllComponents(weaponKey)

    init {
        initializeWeapon()
    }

    private fun initializeWeapon() {
        uiScope.launch {
            weapon.value = getWeaponFromDatabase()
        }
    }

    private suspend fun getWeaponFromDatabase() : Component? {
        return withContext(Dispatchers.IO) {
            val weapon = componentDatabase.getWeapon(weaponKey)
            weapon
        }
    }

    /**
     * Cancelling all jobs
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}