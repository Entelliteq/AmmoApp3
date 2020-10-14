package com.intelliteq.fea.ammocalculator.weaponAmmo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponAmmoDao
import com.intelliteq.fea.ammocalculator.persistence.models.WeaponAmmo
import kotlinx.coroutines.*

/**
 * ViewModel class for WeaponAmmo Fragment
 *
 * @param weaponKey: from Weapon input
 * @param database: WeaponAmmoDao
 */
class WeaponAmmoViewModel(
    private val weaponKey: Long = 0L,
    val database: WeaponAmmoDao
) : ViewModel() {

    //create job and scope of coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //weaponAmmo to watch
    private var weaponAmmo = MutableLiveData<WeaponAmmo?>()


    //reading user input
    val weaponAmmoTypeEditText = MutableLiveData<String>()
    val weaponAmmoDescriptionEditText = MutableLiveData<String>()
    val weaponAmmoDODICEditText = MutableLiveData<String>()
    val weaponAmmoTrainingEditText = MutableLiveData<String>()
    val weaponAmmoSecurityEditText = MutableLiveData<String>()
    val weaponAmmoSustainEditText = MutableLiveData<String>()
    val weaponAmmoLightEditText = MutableLiveData<String>()
    val weaponAmmoMediumEditText = MutableLiveData<String>()
    val weaponAmmoHeavyEditText = MutableLiveData<String>()

    //check if all edit texts are valid mutable live data
    private val _checkStatusOfInputs = MutableLiveData<Boolean>()
    val checkStatusOfInputs: LiveData<Boolean>
        get() = _checkStatusOfInputs

    //Navigation Mutable Live Data
    private val _navigateToInputComponent = MutableLiveData<WeaponAmmo>()
    val navigateToInputComponent: LiveData<WeaponAmmo>
        get() = _navigateToInputComponent

    private val _navigateToAddAnotherAmmo = MutableLiveData<WeaponAmmo>()
    val navigateToAddAnotherAmmo: LiveData<WeaponAmmo>
        get() = _navigateToAddAnotherAmmo

    private val _navigateToConfirmation = MutableLiveData<Long>()
    val navigateToConfirmation: LiveData<Long>
        get() = _navigateToConfirmation

    /**
     * Initializing the weaponAmmo variable
     */
    init {
        initializeAmmo()
        //Log.i("WEapon key", "$weaponKey")
    }

    /**
     * Called from init()
     * Insert the new WeaponAmmo into database
     */
    private fun initializeAmmo() {
        uiScope.launch {
            val newAmmo = WeaponAmmo()
            insert(newAmmo)
            weaponAmmo.value = getAmmoFromDatabase()
        }
    }

    /**
     * Gets weapon ammo from the database that was inserted
     * @return WeaponAmmo object from database
     */
    private suspend fun getAmmoFromDatabase(): WeaponAmmo? {
        return withContext(Dispatchers.IO) {
            var weaponammo = database.getNewAmmo()
            weaponammo
        }
    }

    /**
     * Suspend function to insert into database
     * @param ammo: to be inserted
     */
    private suspend fun insert(ammo: WeaponAmmo) {
        withContext(Dispatchers.IO) {
            database.insert(ammo)
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
                val thisammo = weaponAmmo.value ?: return@launch
                thisammo.weaponId = weaponKey
               //Log.i("This ammo weapon id", "${thisammo.weaponId}")
                //Log.i("This weapon key", "${weaponKey}")
                thisammo.ammoType = weaponAmmoTypeEditText.value.toString()
                thisammo.ammoDescription = weaponAmmoDescriptionEditText.value.toString()
                thisammo.DODIC = weaponAmmoDODICEditText.value.toString()
                thisammo.trainingRate = weaponAmmoTrainingEditText.value!!.toInt()
                thisammo.securityRate = weaponAmmoSecurityEditText.value!!.toInt()
                thisammo.sustainRate = weaponAmmoSustainEditText.value!!.toInt()
                thisammo.lightAssaultRate = weaponAmmoLightEditText.value!!.toInt()
                thisammo.mediumAssaultRate = weaponAmmoMediumEditText.value!!.toInt()
                thisammo.heavyAssaultRate = weaponAmmoHeavyEditText.value!!.toInt()
                update(thisammo)
                _navigateToAddAnotherAmmo.value = thisammo
               // Log.i("WEAPON AMMO updated ", " $thisammo")
            }
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
                thisammo.weaponId = weaponKey
                thisammo.ammoType = weaponAmmoTypeEditText.value.toString()
                thisammo.ammoDescription = weaponAmmoDescriptionEditText.value.toString()
                thisammo.DODIC = weaponAmmoDODICEditText.value.toString()
                thisammo.trainingRate = weaponAmmoTrainingEditText.value!!.toInt()
                thisammo.securityRate = weaponAmmoSecurityEditText.value!!.toInt()
                thisammo.sustainRate = weaponAmmoSustainEditText.value!!.toInt()
                thisammo.lightAssaultRate = weaponAmmoLightEditText.value!!.toInt()
                thisammo.mediumAssaultRate = weaponAmmoMediumEditText.value!!.toInt()
                thisammo.heavyAssaultRate = weaponAmmoHeavyEditText.value!!.toInt()
                update(thisammo)
                _navigateToConfirmation.value = weaponKey
                //Log.i("WEAPON AMMO updated ", " $thisammo")
            }
        }

    }

    /**
     * Verify that all edit text inputs are not null nor empty
     * @return true if all valid
     */
    fun checkEditTexts(): Boolean {
        if (weaponAmmoTrainingEditText.value.isNullOrEmpty() ||
            weaponAmmoSecurityEditText.value.isNullOrEmpty() ||
            weaponAmmoSustainEditText.value.isNullOrEmpty() ||
            weaponAmmoLightEditText.value.isNullOrEmpty() ||
            weaponAmmoMediumEditText.value.isNullOrEmpty() ||
            weaponAmmoHeavyEditText.value.isNullOrEmpty()
        ) {
            _checkStatusOfInputs.value = false
            return false
        } else {
            _checkStatusOfInputs.value = true
            return true
        }

    }

    /**
     * Adding a weapon Ammo using the "Add Component" button
     * Retrieve all edit texts input by user and update database
     */
    fun onAddComponent() {
        if (checkEditTexts()) {
            uiScope.launch {
                val thisammo = weaponAmmo.value ?: return@launch
                thisammo.weaponId = weaponKey
                thisammo.ammoType = weaponAmmoTypeEditText.value.toString()
                thisammo.ammoDescription = weaponAmmoDescriptionEditText.value.toString()
                thisammo.DODIC = weaponAmmoDODICEditText.value.toString()
                thisammo.trainingRate = weaponAmmoTrainingEditText.value!!.toInt()
                thisammo.securityRate = weaponAmmoSecurityEditText.value!!.toInt()
                thisammo.sustainRate = weaponAmmoSustainEditText.value!!.toInt()
                thisammo.lightAssaultRate = weaponAmmoLightEditText.value!!.toInt()
                thisammo.mediumAssaultRate = weaponAmmoMediumEditText.value!!.toInt()
                thisammo.heavyAssaultRate = weaponAmmoHeavyEditText.value!!.toInt()
                update(thisammo)
                _navigateToInputComponent.value = thisammo
                //Log.i("WEAPON AMMO updated ", " $thisammo")
            }
        }
    }

    /**
     * Suspend function to update componentAmmo into database
     * @param ammo: to be updated
     */
    private suspend fun update(ammo: WeaponAmmo) {
        withContext(Dispatchers.IO) {
            database.update(ammo)
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