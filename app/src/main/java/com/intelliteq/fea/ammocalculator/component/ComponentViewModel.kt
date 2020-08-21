package com.intelliteq.fea.ammocalculator.component

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import kotlinx.coroutines.*

/**
 * ViewModel class for Component Fragment
 *
 * @param weaponKey: from Weapon input
 * @param database: ComponentDao
 */
class ComponentViewModel(
    private val weaponKey: Long = 0L,
    val database: ComponentDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //watching component
    private var component = MutableLiveData<Component?>()

    //read user input
    val componentTypeEditText = MutableLiveData<String>()
    val componentDescriptionEditText = MutableLiveData<String>()


    //Navigation Mutable Live Data
    private val _navigateToInputComponentAmmo = MutableLiveData<Component>()
    val navigateToInputComponentAmmo: LiveData<Component>
        get() = _navigateToInputComponentAmmo

    private val _navigateToAnotherComponent = MutableLiveData<Long>()
    val navigateToAnotherComponent: LiveData<Long>
        get() = _navigateToAnotherComponent

    private val _navigateToConfirmation = MutableLiveData<Long>()
    val navigateToConfirmation: LiveData<Long>
        get() = _navigateToConfirmation


    /**
     * Initializing the component variable
     */
    init {
        initializeComponent()
    }

    /**
     * Called from init()
     * Insert the new component into database
     */
    private fun initializeComponent() {
        uiScope.launch {
            val newComponent = Component()
            insert(newComponent)
            component.value = getComponentFromDatabase()
        }
    }

    /**
     * Gets component from the database that was inserted
     * @return Component object from database
     */
    private suspend fun getComponentFromDatabase(): Component? {
        return withContext(Dispatchers.IO) {
            var componentReturned = database.getNewComponent()
            componentReturned
        }
    }


    /**
     * Suspend function to insert into database
     * @param component: to be inserted
     */
    private suspend fun insert(component: Component) {
        withContext(Dispatchers.IO) {
            database.insert(component)
        }
    }

    /**
     * Adding a Component using the "Verify All Inputs" button
     * Retrieve all edit texts input by user and update database
     */
    fun verifyAll() {
        uiScope.launch {
            val thisComponent = component.value ?:return@launch
            thisComponent.weaponId = weaponKey
            thisComponent.componentTypeID = componentTypeEditText.value.toString()
            thisComponent.componentDescription = componentDescriptionEditText.value.toString()
            thisComponent.FEA_id = thisComponent.weaponId.toInt()
            update(thisComponent)
            _navigateToConfirmation.value = weaponKey
            Log.i("WEAPON COMP added ", " ${thisComponent}")
        }

    }

    /**
     * Adding a Component using the "Input Component Ammo" button
     * Retrieve all edit texts input by user and update database
     */
    fun addComponentAmmo() {
        uiScope.launch {
            val thisComponent = component.value ?:return@launch
            thisComponent.weaponId = weaponKey
            thisComponent.componentTypeID = componentTypeEditText.value.toString()
            thisComponent.componentDescription = componentDescriptionEditText.value.toString()
            thisComponent.FEA_id = thisComponent.weaponId.toInt()
            update(thisComponent)
            _navigateToInputComponentAmmo.value = component.value
           Log.i("WEAPON COMP added ", " ${thisComponent}")
        }

    }

    /**
     * Suspend function to update component into database
     * @param component: to be updated
     */
    private suspend fun update(component: Component) {
        withContext(Dispatchers.IO) {
            database.update(component)
        }
    }

    /**
     * Adding a Component using the "Add Another Component" button
     * Retrieve all edit texts input by user and update database
     */
    fun addAnotherComponent() {
        uiScope.launch {
            val thisComponent = component.value ?:return@launch
            thisComponent.weaponId = weaponKey
            thisComponent.componentTypeID = componentTypeEditText.value.toString()
            thisComponent.componentDescription = componentDescriptionEditText.value.toString()
            thisComponent.FEA_id = thisComponent.weaponId.toInt()
            update(thisComponent)
            _navigateToAnotherComponent.value = weaponKey
            Log.i("WEAPON COMP added ", " ${thisComponent}")
        }
    }

    /**
     * Resetting the navigation call to null
     */
    fun doneNavigatingToConfirm() {
        _navigateToConfirmation.value = null
    }

    /**
     * Resetting the navigation call to null
     */
    fun doneNavigatingToComponentAmmo() {
        _navigateToInputComponentAmmo.value = null
    }

    /**
     * Resetting the navigation call to null
     */
    fun doneNavigatingToAnotherComponent() {
        _navigateToAnotherComponent.value = null
        initializeComponent()
    }

    /**
     * Cancelling all jobs
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
