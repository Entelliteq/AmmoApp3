package com.intelliteq.fea.ammocalculator.weaponAmmo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import kotlinx.coroutines.*

/**
 * ViewModel class for WeaponAmmo Fragment
 *
 * @param weaponKey: from Weapon input
 * @param database: AmmoDao
 */
class WeaponAmmoViewModel(
    private val weaponKey: Long = 0L,
    val database: AmmoDao,
    private val compDatabase: ComponentDao
) : ViewModel() {

    //create job and scope of coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //weaponAmmo to watch
    private var weaponAmmo = MutableLiveData<Ammo?>()


    val weaponAmmoDescriptionEditText = MutableLiveData<String>()
    val weaponAmmoDODICEditText = MutableLiveData<String>()
    val weaponAmmoTrainingEditText = MutableLiveData<String>()
    val weaponAmmoSecurityEditText = MutableLiveData<String>()
    val weaponAmmoSustainEditText = MutableLiveData<String>()
    val weaponAmmoLightEditText = MutableLiveData<String>()
    val weaponAmmoMediumEditText = MutableLiveData<String>()
    val weaponAmmoHeavyEditText = MutableLiveData<String>()
    val weaponAmmoDefaultAmmo = MutableLiveData<Boolean>()

    var ammosWeapon = listOf<Ammo>()

    //check if all edit texts are valid mutable live data
    private val _checkStatusOfInputs = MutableLiveData<Boolean>()
    val checkStatusOfInputs: LiveData<Boolean>
        get() = _checkStatusOfInputs

    //Navigation Mutable Live Data
    private val _navigateToInputComponent = MutableLiveData<Long>()
    val navigateToInputComponent: LiveData<Long>
        get() = _navigateToInputComponent

    private val _navigateToAddAnotherAmmo = MutableLiveData<Ammo>()
    val navigateToAddAnotherAmmo: LiveData<Ammo>
        get() = _navigateToAddAnotherAmmo

    private val _navigateToConfirmation = MutableLiveData<Long>()
    val navigateToConfirmation: LiveData<Long>
        get() = _navigateToConfirmation

    /**
     * Initializing the weaponAmmo variable
     */
    init {
        initializeAmmo()
        weaponAmmoDefaultAmmo.value = false

        //Log.i("Weapon key", "$weaponKey")
    }

    /**
     * Called from init()
     * Insert the new WeaponAmmo into database
     */
    private fun initializeAmmo() {
        uiScope.launch {
            val newAmmo = Ammo()
            insert(newAmmo)
            ammosWeapon = getListOfAmmoFromDatabase()
            weaponAmmo.value = getAmmoFromDatabase()
            //Log.i("Weapon amo1", "${weaponAmmo.value!!.ammoAutoId}")
        }
    }

    /**
     * Gets weapon ammo from the database that was inserted
     * @return WeaponAmmo object from database
     */
    private suspend fun getAmmoFromDatabase(): Ammo? {
        return withContext(Dispatchers.IO) {
            val weaponammo = database.getNewAmmo()
            //Log.i("Weapon ammo2 auto", "${weaponammo!!.ammoAutoId}")
            weaponammo
        }
    }

    private suspend fun getListOfAmmoFromDatabase(): List<Ammo> {
        return withContext(Dispatchers.IO) {
            val weaponammo = database.getAllWeaponAmmoList(weaponKey)
            //Log.i("Weapon ammo2 auto", "${weaponammo!!.ammoAutoId}")
            weaponammo
        }
    }

    /**
     * Suspend function to insert into database
     * @param ammo: to be inserted
     */
    private suspend fun insert(ammo: Ammo) {
        withContext(Dispatchers.IO) {
            database.insert(ammo)
            //Log.i("Weapon amo3", "${ammo.ammoAutoId}")
        }
    }

    /**
     * Resetting the navigation call to null
     */
    fun doneNavigatingToComp() {
        _navigateToInputComponent.value = null

    }

    /**
     * Resetting the navigation call to null
     */
    fun doneNavigatingToAmmo() {
        _navigateToAddAnotherAmmo.value = null
        initializeAmmo()
    }

    /**
     * Adding a weapon ammo using the "Add Another Ammo" button
     * Retrieve all edit texts input by user and update database
     */
    fun onAddAnotherAmmo() {
        if (checkEditTexts()) {
            uiScope.launch {
                val thisAmmo = weaponAmmo.value ?: return@launch

                thisAmmo.componentId = getComponentID(weaponKey)  //changed 10/27
                thisAmmo.ammoDescription = weaponAmmoDescriptionEditText.value.toString()
                thisAmmo.ammoDODIC = weaponAmmoDODICEditText.value.toString()
                thisAmmo.trainingRate = weaponAmmoTrainingEditText.value!!.toInt()
                thisAmmo.securityRate = weaponAmmoSecurityEditText.value!!.toInt()
                thisAmmo.weaponId =weaponKey
                thisAmmo.defaultAmmo = weaponAmmoDefaultAmmo.value!!
                thisAmmo.primaryAmmo = true
                thisAmmo.sustainRate = weaponAmmoSustainEditText.value!!.toInt()
                thisAmmo.lightAssaultRate = weaponAmmoLightEditText.value!!.toInt()
                thisAmmo.mediumAssaultRate = weaponAmmoMediumEditText.value!!.toInt()
                thisAmmo.heavyAssaultRate = weaponAmmoHeavyEditText.value!!.toInt()
                update(thisAmmo)
                _navigateToAddAnotherAmmo.value = thisAmmo
                Log.i("REDO Another AMMO", " $thisAmmo")
            }
        }
    }

    //changed to return compAutoID in DB 12/1
    private suspend fun getComponentID(idEntered: Long)  : Long {
        return withContext(Dispatchers.IO) {
            val id = compDatabase.getCompID(idEntered)
            id
        }
    }

    /**
     * Adding a weapon ammo using the "Verify All Inputs" button
     * Retrieve all edit texts input by user and update database
     */
    fun verify() {
        if (checkEditTexts()) {
            uiScope.launch {
                val thisammo = weaponAmmo.value ?: return@launch
                thisammo.componentId = getComponentID(weaponKey)
                //    thisammo.ammoTypeId = weaponAmmoTypeEditText.value.toString()
                thisammo.ammoDescription = weaponAmmoDescriptionEditText.value.toString()
                thisammo.ammoDODIC = weaponAmmoDODICEditText.value.toString()
                thisammo.trainingRate = weaponAmmoTrainingEditText.value!!.toInt()
                thisammo.securityRate = weaponAmmoSecurityEditText.value!!.toInt()
                thisammo.weaponId =weaponKey
                thisammo.defaultAmmo = weaponAmmoDefaultAmmo.value!!
                thisammo.primaryAmmo = true
                thisammo.sustainRate = weaponAmmoSustainEditText.value!!.toInt()
                thisammo.lightAssaultRate = weaponAmmoLightEditText.value!!.toInt()
                thisammo.mediumAssaultRate = weaponAmmoMediumEditText.value!!.toInt()
                thisammo.heavyAssaultRate = weaponAmmoHeavyEditText.value!!.toInt()
                update(thisammo)
                _navigateToConfirmation.value = weaponKey
                Log.i("REDO Verify AMMO", " $thisammo")
            }
        }

    }

    /**
     * Verify that all edit text inputs are not null nor empty
     * @return true if all valid
     */
    private fun checkEditTexts(): Boolean {
        return if (weaponAmmoTrainingEditText.value.isNullOrEmpty() ||
            weaponAmmoSecurityEditText.value.isNullOrEmpty() ||
            weaponAmmoSustainEditText.value.isNullOrEmpty() ||
            weaponAmmoLightEditText.value.isNullOrEmpty() ||
            weaponAmmoMediumEditText.value.isNullOrEmpty() ||
            weaponAmmoHeavyEditText.value.isNullOrEmpty()
        ) {
            _checkStatusOfInputs.value = false
            false
        } else {
            _checkStatusOfInputs.value = true
            true
        }

    }

    /**
     * Adding a weapon Ammo using the "Add Component" button
     * Retrieve all edit texts input by user and update database
     */
    fun onAddComponent() {
        if (checkEditTexts()) {
            uiScope.launch {
                val thisAmmo = weaponAmmo.value ?: return@launch
                thisAmmo.componentId = getComponentID(weaponKey)
                thisAmmo.ammoDescription = weaponAmmoDescriptionEditText.value.toString()
                thisAmmo.ammoDODIC = weaponAmmoDODICEditText.value.toString()
                thisAmmo.weaponId =weaponKey
                thisAmmo.primaryAmmo = true
                thisAmmo.defaultAmmo = weaponAmmoDefaultAmmo.value!!
                thisAmmo.trainingRate = weaponAmmoTrainingEditText.value!!.toInt()
                thisAmmo.securityRate = weaponAmmoSecurityEditText.value!!.toInt()
                thisAmmo.sustainRate = weaponAmmoSustainEditText.value!!.toInt()
                thisAmmo.lightAssaultRate = weaponAmmoLightEditText.value!!.toInt()
                thisAmmo.mediumAssaultRate = weaponAmmoMediumEditText.value!!.toInt()
                thisAmmo.heavyAssaultRate = weaponAmmoHeavyEditText.value!!.toInt()
                update(thisAmmo)
                _navigateToInputComponent.value = weaponKey
               Log.i("REDO Add comp AMMO", " $thisAmmo")
            }
        }
    }

    /**
     * Suspend function to update componentAmmo into database
     * @param ammo: to be updated
     */
    private suspend fun update(ammo: Ammo) {
        withContext(Dispatchers.IO) {
            database.update(ammo)
           // Log.i("Weapon amo7", "${ammo.ammoAutoId}")
        }
    }

    fun setDefaultAmmo(default: Boolean) {
        uiScope.launch {
            weaponAmmoDefaultAmmo.value  = default
            if(default) {
                for (ammo in ammosWeapon) {
                    if(ammo.defaultAmmo){
                        ammo.defaultAmmo = false
                        update(ammo)
                    }
                }
            }
            //check for accuracy
            for (ammo in ammosWeapon) {
                Log.i("BOX3", "${ammo.ammoDODIC} ${ammo.defaultAmmo}")
            }
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