package com.intelliteq.fea.ammocalculator.componentAmmo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import kotlinx.coroutines.*

/**
 * ViewModel class for ComponentAmmo Fragment
 *
 * @param componentKey: from Component input
 * @param database: AmmoDao
 */
class ComponentAmmoViewModel (
    private val componentKey: Long = 0L,
    private val weaponKey: Long = 0L,
    val database: AmmoDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //watching componentAmmo
    private var componentAmmo = MutableLiveData<Ammo?>()

    //read user input
    val componentAmmoTypeEditText = MutableLiveData<String>()
    val componentAmmoDescriptionEditText = MutableLiveData<String>()
    val componentAmmoDODICEditText = MutableLiveData<String>()
    val componentAmmoTrainingEditText = MutableLiveData<String>()
    val componentAmmoSecurityEditText = MutableLiveData<String>()
    val componentAmmoSustainEditText = MutableLiveData<String>()
    val componentAmmoLightEditText = MutableLiveData<String>()
    val componentAmmoMediumEditText = MutableLiveData<String>()
    val componentAmmoHeavyEditText = MutableLiveData<String>()


    //check if all edit texts are filled
    private val _checkStatusOfInputs = MutableLiveData<Boolean>()
    val checkStatusOfInputs: LiveData<Boolean>
        get() = _checkStatusOfInputs

    //Navigation Mutable Live Data
    private val _navigateToInputAnotherComponentAmmo = MutableLiveData<Array<Long>>()
    val navigateToInputAnotherComponentAmmo: LiveData<Array<Long>>
        get() = _navigateToInputAnotherComponentAmmo

    private val _navigateToConfirmation = MutableLiveData<Long>()
    val navigateToConfirmation: LiveData<Long>
        get() = _navigateToConfirmation

    private val _navigateToAnotherComponent = MutableLiveData<Long>()
    val navigateToAnotherComponent: LiveData<Long>
        get() = _navigateToAnotherComponent

    var keys = arrayOf(weaponKey, componentKey) //switched order 12/1

    /**
     * Initializing the componentAmmo variable
     */
    init {
        initializeComponentAmmo()
       // Log.i("COMP1", "initialized ammo")

    }

    /**
     * Called from init()
     * Insert the new componentAmmo into database
     */
    private fun initializeComponentAmmo() {
        uiScope.launch {
            val newComponentAmmo = Ammo()
            insert(newComponentAmmo)
            componentAmmo.value = getComponentAmmoFromDatabase()
        }
    }

    /**
     * Gets componentAmmo from the database that was inserted
     * @return ComponentAmmo object from database
     */
    private suspend fun getComponentAmmoFromDatabase() : Ammo? {
        return withContext(Dispatchers.IO) {
            var compammo = database.getNewAmmo()
            compammo
        }
    }

    /**
     * Suspend function to insert into database
     * @param compAmmo: to be inserted
     */
    private suspend fun insert(compAmmo: Ammo) {
        withContext(Dispatchers.IO) {
            database.insert(compAmmo)
        }
    }

    /**
     * Resetting the navigation call to null
     */
    fun doneNavigatingToVerify() {
        _navigateToConfirmation.value = null
    }

    fun doneNavigateToAnotherComponent() {
        _navigateToAnotherComponent.value = null
    }

    /**
     * Adding a ComponentAmmo using the "Add Another Ammo" button
     * Retrieve all edit texts input by user and update database
     */
    fun addAnotherAmmo() {
        if(checkEditTexts()) {
            uiScope.launch {
                val thisCompAmmo = componentAmmo.value ?: return@launch
               // thisCompAmmo.ammoTypeId = componentAmmoTypeEditText.value.toString()
                thisCompAmmo.ammoDescription = componentAmmoDescriptionEditText.value.toString()
                thisCompAmmo.ammoDODIC = componentAmmoDODICEditText.value.toString()
                thisCompAmmo.componentId = componentKey
                thisCompAmmo.weaponId = weaponKey
                thisCompAmmo.trainingRate = componentAmmoTrainingEditText.value!!.toInt()
                thisCompAmmo.securityRate = componentAmmoSecurityEditText.value!!.toInt()
                thisCompAmmo.sustainRate = componentAmmoSustainEditText.value!!.toInt()
                thisCompAmmo.lightAssaultRate = componentAmmoLightEditText.value!!.toInt()
                thisCompAmmo.mediumAssaultRate = componentAmmoMediumEditText.value!!.toInt()
                thisCompAmmo.heavyAssaultRate = componentAmmoHeavyEditText.value!!.toInt()

                update(thisCompAmmo)
                _navigateToInputAnotherComponentAmmo.value = keys
                Log.i("CompAmmo another ammo", " $thisCompAmmo")

            }
        }
    }

    /**
     * Adding a ComponentAmmo using the "Add Another Ammo" button
     * Retrieve all edit texts input by user and update database
     */
    fun addAnotherComponent() {
        if(checkEditTexts()) {
            uiScope.launch {
                val thisCompAmmo = componentAmmo.value ?: return@launch
               // thisCompAmmo.ammoTypeId = componentAmmoTypeEditText.value.toString()
                thisCompAmmo.ammoDescription = componentAmmoDescriptionEditText.value.toString()
                thisCompAmmo.ammoDODIC = componentAmmoDODICEditText.value.toString()
                thisCompAmmo.componentId = componentKey
                thisCompAmmo.weaponId = weaponKey
                thisCompAmmo.trainingRate = componentAmmoTrainingEditText.value!!.toInt()
                thisCompAmmo.securityRate = componentAmmoSecurityEditText.value!!.toInt()
                thisCompAmmo.sustainRate = componentAmmoSustainEditText.value!!.toInt()
                thisCompAmmo.lightAssaultRate = componentAmmoLightEditText.value!!.toInt()
                thisCompAmmo.mediumAssaultRate = componentAmmoMediumEditText.value!!.toInt()
                thisCompAmmo.heavyAssaultRate = componentAmmoHeavyEditText.value!!.toInt()
                update(thisCompAmmo)
                _navigateToAnotherComponent.value = weaponKey
                Log.i("CompAmmo another comp ", " $thisCompAmmo")
            }
        }
    }



    /**
     * Check if all the Edit Texts are filled
     * @return Boolean that is true if they are valid
     */
    fun checkEditTexts() : Boolean {
        if (//componentAmmoTypeEditText.value.isNullOrEmpty() ||
                componentAmmoDODICEditText.value.isNullOrEmpty() ||
                componentAmmoDescriptionEditText.value.isNullOrEmpty()) {
            _checkStatusOfInputs.value = false
            return false
        } else {
            _checkStatusOfInputs.value = true
            return true
        }
    }

    /**
     * Suspend function to update componentAmmo into database
     * @param compAmmo: to be updated
     */
    private suspend fun update(compAmmo: Ammo) {
        withContext(Dispatchers.IO) {
            database.update(compAmmo)
        }
    }


    /**
     * Adding a ComponentAmmo using the "Verify All Inputs" button
     * Retrieve all edit texts input by user and update database
     */
    fun verify() {
        if(checkEditTexts()) {
            uiScope.launch {
                val thisCompAmmo = componentAmmo.value ?: return@launch
                //thisCompAmmo.ammoTypeId = componentAmmoTypeEditText.value.toString()
                thisCompAmmo.ammoDescription = componentAmmoDescriptionEditText.value.toString()
                thisCompAmmo.ammoDODIC = componentAmmoDODICEditText.value.toString()
                thisCompAmmo.componentId = componentKey
                thisCompAmmo.weaponId = weaponKey
                thisCompAmmo.trainingRate = componentAmmoTrainingEditText.value!!.toInt()
                thisCompAmmo.securityRate = componentAmmoSecurityEditText.value!!.toInt()
                thisCompAmmo.sustainRate = componentAmmoSustainEditText.value!!.toInt()
                thisCompAmmo.lightAssaultRate = componentAmmoLightEditText.value!!.toInt()
                thisCompAmmo.mediumAssaultRate = componentAmmoMediumEditText.value!!.toInt()
                thisCompAmmo.heavyAssaultRate = componentAmmoHeavyEditText.value!!.toInt()
                update(thisCompAmmo)
                _navigateToConfirmation.value = weaponKey
                Log.i("CompAmmo verify", " $thisCompAmmo")

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