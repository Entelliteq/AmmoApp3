package com.intelliteq.fea.ammocalculator.componentAmmo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentAmmoDao
import com.intelliteq.fea.ammocalculator.persistence.models.ComponentAmmo
import kotlinx.coroutines.*

/**
 * ViewModel class for ComponentAmmo Fragment
 *
 * @param componentKey: from Component input
 * @param database: ComponentAmmoDao
 */
class ComponentAmmoViewModel (
    private val componentKey: Long = 0L,
    private val weaponKey: Long = 0L,
    val database: ComponentAmmoDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //watching componentAmmo
    private var componentAmmo = MutableLiveData<ComponentAmmo?>()

    //read user input
    val componentAmmoTypeEditText = MutableLiveData<String>()
    val componentAmmoDescriptionEditText = MutableLiveData<String>()
    val componentAmmoDODICEditText = MutableLiveData<String>()

    //check if all edit texts are filled
    private val _checkStatusOfInputs = MutableLiveData<Boolean>()
    val checkStatusOfInputs: LiveData<Boolean>
        get() = _checkStatusOfInputs

    //Navigation Mutable Live Data
    private val _navigateToInputAnotherComponentAmmo = MutableLiveData<ComponentAmmo>()
    val navigateToInputAnotherComponentAmmo: LiveData<ComponentAmmo>
        get() = _navigateToInputAnotherComponentAmmo

    private val _navigateToConfirmation = MutableLiveData<ComponentAmmo>()
    val navigateToConfirmation: LiveData<ComponentAmmo>
        get() = _navigateToConfirmation

    private val _navigateToAnotherComponent = MutableLiveData<ComponentAmmo>()
    val navigateToAnotherComponent: LiveData<ComponentAmmo>
        get() = _navigateToAnotherComponent

    /**
     * Initializing the componentAmmo variable
     */
    init {
        initializeComponentAmmo()

    }

    /**
     * Called from init()
     * Insert the new componentAmmo into database
     */
    private fun initializeComponentAmmo() {
        uiScope.launch {
            val newComponentAmmo = ComponentAmmo()
            insert(newComponentAmmo)
            componentAmmo.value = getComponentAmmoFromDatabase()
        }
    }

    /**
     * Gets componentAmmo from the database that was inserted
     * @return ComponentAmmo object from database
     */
    private suspend fun getComponentAmmoFromDatabase() : ComponentAmmo? {
        return withContext(Dispatchers.IO) {
            var compammo = database.getNewComponentAmmo()
            compammo
        }
    }

    /**
     * Suspend function to insert into database
     * @param compAmmo: to be inserted
     */
    private suspend fun insert(compAmmo: ComponentAmmo) {
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
                thisCompAmmo.ammoTypeID = componentAmmoTypeEditText.value.toString()
                thisCompAmmo.ammoDescription = componentAmmoDescriptionEditText.value.toString()
                thisCompAmmo.ammoDODIC = componentAmmoDODICEditText.value.toString()
                thisCompAmmo.componentId = componentKey
                thisCompAmmo.weaponIdComponentAmmo = weaponKey
                update(thisCompAmmo)
                _navigateToInputAnotherComponentAmmo.value = thisCompAmmo
                //Log.i("WEAPON CompAmmo update ", " $thisCompAmmo")

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
                thisCompAmmo.ammoTypeID = componentAmmoTypeEditText.value.toString()
                thisCompAmmo.ammoDescription = componentAmmoDescriptionEditText.value.toString()
                thisCompAmmo.ammoDODIC = componentAmmoDODICEditText.value.toString()
                thisCompAmmo.componentId = componentKey
                thisCompAmmo.weaponIdComponentAmmo = weaponKey
                update(thisCompAmmo)
                _navigateToAnotherComponent.value = thisCompAmmo
                //Log.i("WEAPON CompAmmo update ", " $thisCompAmmo")
            }
        }
    }



    /**
     * Check if all the Edit Texts are filled
     * @return Boolean that is true if they are valid
     */
    fun checkEditTexts() : Boolean {
        if (componentAmmoTypeEditText.value.isNullOrEmpty() ||
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
    private suspend fun update(compAmmo: ComponentAmmo) {
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
                thisCompAmmo.ammoTypeID = componentAmmoTypeEditText.value.toString()
                thisCompAmmo.ammoDescription = componentAmmoDescriptionEditText.value.toString()
                thisCompAmmo.ammoDODIC = componentAmmoDODICEditText.value.toString()
                thisCompAmmo.componentId = componentKey
                thisCompAmmo.weaponIdComponentAmmo = weaponKey
                update(thisCompAmmo)
                _navigateToConfirmation.value = thisCompAmmo
                //Log.i("WEAPON CompAmmo update ", " $thisCompAmmo")

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