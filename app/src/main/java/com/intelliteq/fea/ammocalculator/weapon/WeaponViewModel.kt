package com.intelliteq.fea.ammocalculator.weapon

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import kotlinx.coroutines.*


/**
 * ViewModel class for Weapon Fragment
 *
 * @param application: the application returned
 * @param database: WeaponDao
 */
class WeaponViewModel (
    val weaponDatabase: WeaponDao,
    val componentDatabase: ComponentDao,
    application: Application) : AndroidViewModel(application) {

    //create job and scope of coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    //create weapon to watch
    private var weapon = MutableLiveData<Weapon?>()
    private var component = MutableLiveData<Component?>()



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
            val newComponent = Component()
            insertWeapon(newWeapon)
            weapon.value = getWeaponFromDatabase()
            newComponent.weaponId = weapon.value!!.weaponAutoId
            insertComponent(newComponent)
            component.value = getComponentFromDatabase()

           // Log.i("WEAPON updated", "////W: ${weapon.value}")
           // Log.i("WEAPON updated", "////C: ${component.value}")
        }
    }

    /**
     * Gets weapon from the database that was inserted
     * @return Weapon object from database
     */
    private suspend fun getWeaponFromDatabase() : Weapon? {
        return withContext(Dispatchers.IO) {
            var weapon = weaponDatabase.getNewWeapon()
            weapon
        }
    }

    /**
     * Gets component from the database that was inserted
     * @return Component object from database
     */
    private suspend fun getComponentFromDatabase() : Component? {
        return withContext(Dispatchers.IO) {
            var comp = componentDatabase.getNewComponent()
            comp
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
            val thisComponent = component.value?: return@launch
            thisComponent.componentDescription = weaponDescriptionEditText.value.toString()
            thisComponent.componentTypeId = weaponTypeEditText.value.toString()
            thisComponent.FEA_id = thisComponent.componentAutoId.toInt()
            thisComponent.primaryWeapon = true
            weapon.value!!.componentId = thisComponent.componentAutoId
            updateWeapon(weapon.value!!)
            updateComponent(thisComponent)
          //  Log.i("called HERE", "weap")
            _navigateToInputWeaponAmmo.value = weapon.value
          //  Log.i("WEAPON updated", "////W: ${weapon.value}")
          //  Log.i("WEAPON updated", "////C: ${thisComponent}")


        }

    }

    /**
     * Suspend function to insert into database
     * @param weapon: to be inserted
     */
    private suspend fun insertWeapon(weapon: Weapon) {
        withContext(Dispatchers.IO) {
            weaponDatabase.insert(weapon)
        }
    }

    /**
     * Suspend function to insert into database
     * @param comp: to be inserted
     */
    private suspend fun insertComponent(comp: Component) {
        withContext(Dispatchers.IO) {
            componentDatabase.insert(comp)
        }
    }

    /**
     * Suspend function to update componentAmmo into database
     * @param weapon: to be updated
     */
    private suspend fun updateWeapon(weapon: Weapon) {
        withContext(Dispatchers.IO) {
            weaponDatabase.update(weapon)
        }
    }

     /**
     * Suspend function to update componentAmmo into database
     * @param comp: to be updated
     */
    private suspend fun updateComponent(comp: Component) {
        withContext(Dispatchers.IO) {
            componentDatabase.update(comp)
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