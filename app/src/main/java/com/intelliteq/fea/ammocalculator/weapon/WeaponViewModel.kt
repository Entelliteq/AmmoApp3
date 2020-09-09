package com.intelliteq.fea.ammocalculator.weapon

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import kotlinx.coroutines.*
import java.nio.file.WatchEvent

/**
 * ViewModel class for Weapon Fragment
 *
 * @param application: the application returned
 * @param database: WeaponDao
 */
class WeaponViewModel (
    val database: WeaponDao,
    application: Application) : AndroidViewModel(application) {

    //create job and scope of coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    //create weapon to watch
    private var weapon = MutableLiveData<Weapon?>()
    lateinit var weapons : ArrayList<Weapon>


    //reading the user input
    val weaponDescriptionEditText = MutableLiveData<String>()
    val weaponTypeEditText = MutableLiveData<String>()


    //wrapped navigation to input weapon ammo
    private val _navigateToInputWeaponAmmo = MutableLiveData<Weapon>()
    val navigateToInputWeaponAmmo: LiveData<Weapon>
        get() = _navigateToInputWeaponAmmo



    /**
     * Initializing the weapon variable
     */
    init {
        initializeWeapon()
    }

    /**
     * Called from init()
     * Insert the new weapon into database
     */
    private fun initializeWeapon() {
        uiScope.launch {
            val newWeapon = Weapon()
            insert(newWeapon)
            weapon.value = getWeaponFromDatabase()
        }
    }

    /**
     * Gets weapon from the database that was inserted
     * @return Weapon object from database
     */
    private suspend fun getWeaponFromDatabase() : Weapon? {
        return withContext(Dispatchers.IO) {
            var weapon = database.getNewWeapon()
            weapon
        }
    }

    /**
     * Resetting the navigation call to null
     */
    fun doneNavigation() {
        _navigateToInputWeaponAmmo.value = null
    }

    /**
     * Adding a Weapon using the "Input Ammo" button
     * Retrieve all edit texts input by user and update database
     */
    fun onInputAmmo() {
        uiScope.launch {
            val thisWeapon = weapon.value?: return@launch
            thisWeapon.weaponDescription = weaponDescriptionEditText.value.toString()
            thisWeapon.weaponTypeID = weaponTypeEditText.value.toString()
            thisWeapon.FEA_id = thisWeapon.weaponAutoId.toInt()
            update(thisWeapon)
            _navigateToInputWeaponAmmo.value = weapon.value
            Log.i("WEAPON updated", "///// ${thisWeapon}")


        }

    }

    /**
     * Suspend function to insert into database
     * @param weapon: to be inserted
     */
    private suspend fun insert(weapon: Weapon) {
        withContext(Dispatchers.IO) {
            database.insert(weapon)
        }


    }

    /**
     * Suspend function to update componentAmmo into database
     * @param weapon: to be updated
     */
    private suspend fun update(weapon: Weapon) {
        withContext(Dispatchers.IO) {
            database.update(weapon)
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