package com.intelliteq.fea.ammocalculator.weapon

import android.app.Application
import android.app.ApplicationErrorReport
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import kotlinx.coroutines.*

class WeaponViewModel (
    val database: WeaponDao,
    application: Application) : AndroidViewModel(application) {

    //create job and scope of coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    //create weapon to watch
    private var weapon = MutableLiveData<Weapon?>()

    //reading the user input
    val weaponDescriptionEditText = MutableLiveData<String>()
    val weaponTypeEditText = MutableLiveData<String>()

    //wrapped navigation to input weapon ammo
    private val _navigateToInputWeaponAmmo = MutableLiveData<Weapon>()
    val navigateToInputWeaponAmmo: LiveData<Weapon>
        get() = _navigateToInputWeaponAmmo



    //initialize weapon
    init {
        initializeWeapon()
    }

    private fun initializeWeapon() {
        uiScope.launch {
            val newWeapon = Weapon()
            insert(newWeapon)
            weapon.value = getWeaponFromDatabase()
        }
    }

    private suspend fun getWeaponFromDatabase() : Weapon? {
        return withContext(Dispatchers.IO) {
            var weapon = database.getNewWeapon()
            weapon
        }
    }

    //resets variable that triggers navigation
    fun doneNavigation() {
        _navigateToInputWeaponAmmo.value = null
    }

    //button to move to input ammo; insert a new weapon into database
    fun onInputAmmo() {
        uiScope.launch {
            val thisWeapon = weapon.value?: return@launch
            thisWeapon.weaponDescription = weaponDescriptionEditText.value.toString()
            thisWeapon.weaponTypeID = weaponTypeEditText.value.toString()
            update(thisWeapon)
            _navigateToInputWeaponAmmo.value = weapon.value
            Log.i("WEAPON added", "///// ${thisWeapon}")
        }

    }

    private suspend fun insert(weapon: Weapon) {
        withContext(Dispatchers.IO) {
            database.insert(weapon)
        }
    }

    private suspend fun update(weapon: Weapon) {
        withContext(Dispatchers.IO) {
            database.update(weapon)
        }
    }

    //cancel all coroutines
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}