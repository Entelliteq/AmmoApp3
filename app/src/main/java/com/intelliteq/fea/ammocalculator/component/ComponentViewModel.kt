package com.intelliteq.fea.ammocalculator.component

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import kotlinx.coroutines.*

class ComponentViewModel(
    private val weaponKey: Long = 0L,
    val database: ComponentDao
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var component = MutableLiveData<Component?>()

    //read user input
    val componentTypeEditText = MutableLiveData<String>()
    val componentDescriptionEditText = MutableLiveData<String>()


    private val _navigateToInputComponentAmmo = MutableLiveData<Component>()
    val navigateToInputComponentAmmo: LiveData<Component>
        get() = _navigateToInputComponentAmmo

    private val _navigateToAnotherComponent = MutableLiveData<Long>()
    val navigateToAnotherComponent: LiveData<Long>
        get() = _navigateToAnotherComponent

    private val _navigateToConfirmation = MutableLiveData<Long>()
    val navigateToConfirmation: LiveData<Long>
        get() = _navigateToConfirmation


    init {
        initializeComponent()
    }

    private fun initializeComponent() {
        uiScope.launch {
            val newComponent = Component()
            insert(newComponent)
            component.value = getComponentFromDatabase()
        }
    }

    private suspend fun getComponentFromDatabase(): Component? {
        return withContext(Dispatchers.IO) {
            var componentReturned = database.getNewComponent()
            componentReturned
        }
    }


    private suspend fun insert(component: Component) {
        withContext(Dispatchers.IO) {
            database.insert(component)
        }
    }

    fun verifyAll() {
        uiScope.launch {
            val thisComponent = component.value ?:return@launch
            thisComponent.weaponId = weaponKey
            thisComponent.componentTypeID = componentTypeEditText.value.toString()
            thisComponent.componentDescription = componentDescriptionEditText.value.toString()
            update(thisComponent)
            _navigateToConfirmation.value = weaponKey
        }

    }

    fun addComponentAmmo() {

        uiScope.launch {
            val thisComponent = component.value ?:return@launch
            thisComponent.weaponId = weaponKey
            thisComponent.componentTypeID = componentTypeEditText.value.toString()
            thisComponent.componentDescription = componentDescriptionEditText.value.toString()
            update(thisComponent)
            _navigateToInputComponentAmmo.value = component.value
        }

    }

    private suspend fun update(component: Component) {
        withContext(Dispatchers.IO) {
            database.update(component)
        }
    }

    fun addAnotherComponent() {
        uiScope.launch {
            val thisComponent = component.value ?:return@launch
            thisComponent.weaponId = weaponKey
            thisComponent.componentTypeID = componentTypeEditText.value.toString()
            thisComponent.componentDescription = componentDescriptionEditText.value.toString()
            update(thisComponent)
            _navigateToAnotherComponent.value = weaponKey
        }
    }

    fun doneNavigatingToConfirm() {
        _navigateToConfirmation.value = null
    }

    fun doneNavigatingToComponentAmmo() {
        _navigateToInputComponentAmmo.value = null
    }


    fun doneNavigatingToAnotherComponent() {
        _navigateToAnotherComponent.value = null
        initializeComponent()
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
