package com.intelliteq.fea.ammocalculator.componentAmmo

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentAmmoDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.ComponentAmmo
import kotlinx.coroutines.*

class ComponentAmmoViewModel (
    private val componentKey: Long = 0L,
    private val weaponKey: Long = 0L,
    val database: ComponentAmmoDao
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var componentAmmo = MutableLiveData<ComponentAmmo?>()

    //read user input
    val componentAmmoTypeEditText = MutableLiveData<String>()
    val componentAmmoDescriptionEditText = MutableLiveData<String>()
    val componentAmmoDODICEditText = MutableLiveData<String>()

    private val _checkStatusOfInputs = MutableLiveData<Boolean>()
    val checkStatusOfInputs: LiveData<Boolean>
        get() = _checkStatusOfInputs

    private val _navigateToInputAnotherComponentAmmo = MutableLiveData<ComponentAmmo>()
    val navigateToInputAnotherComponentAmmo: LiveData<ComponentAmmo>
        get() = _navigateToInputAnotherComponentAmmo

    private val _navigateToConfirmation = MutableLiveData<ComponentAmmo>()
    val navigateToConfirmation: LiveData<ComponentAmmo>
        get() = _navigateToConfirmation


    init {
        initializeComponentAmmo()
    }

    private fun initializeComponentAmmo() {
        uiScope.launch {
            val newComponentAmmo = ComponentAmmo()
            insert(newComponentAmmo)
            componentAmmo.value = getComponentAmmoFromDatabase()
        }
    }

    private suspend fun getComponentAmmoFromDatabase() : ComponentAmmo? {
        return withContext(Dispatchers.IO) {
            var compammo = database.getNewComponentAmmo()
            compammo
        }
    }

    private suspend fun insert(compAmmo: ComponentAmmo) {
        withContext(Dispatchers.IO) {
            database.insert(compAmmo)
        }
    }

    fun doneNavigatingToCompAmmo() {
        _navigateToInputAnotherComponentAmmo.value = null
    }

    fun doneNavigatingToVerify() {
        _navigateToConfirmation.value = null
    }


    fun addAnotherAmmo() {
        if(checkEditTexts()) {
            uiScope.launch {
                val thisCompAmmo = componentAmmo.value ?: return@launch
                thisCompAmmo.componentAmmoTypeID = componentAmmoTypeEditText.value.toString()
                thisCompAmmo.componentAmmoDescription = componentAmmoDescriptionEditText.value.toString()
                thisCompAmmo.componentAmmoDODIC = componentAmmoDODICEditText.value.toString()
                thisCompAmmo.componentId = componentKey
                update(thisCompAmmo)
                _navigateToInputAnotherComponentAmmo.value = thisCompAmmo
                Log.i("WEAPON COMP AMMO id ", " ${thisCompAmmo.componentAmmoId}")
                Log.i("WEAPON COMP_AMMO wepId ", " ${thisCompAmmo.weaponIdComponentAmmo}")
                Log.i("WEAPON COMP AMMO ty ", " ${thisCompAmmo.componentAmmoTypeID}")
                Log.i("WEAPON COMP AMMO des ", " ${thisCompAmmo.componentAmmoDescription}")
                Log.i("WEAPON COMP AMMO dod ", " ${thisCompAmmo.componentAmmoDODIC}")
                Log.i("WEAPON COMP AMMO c_id ", " ${thisCompAmmo.componentAmmoId}")
            }
        }
    }

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

    private suspend fun update(compAmmo: ComponentAmmo) {
        withContext(Dispatchers.IO) {
            database.update(compAmmo)
        }
    }

    fun verify() {
        if(checkEditTexts()) {
            uiScope.launch {
                val thisCompAmmo = componentAmmo.value ?: return@launch
                thisCompAmmo.componentAmmoTypeID = componentAmmoTypeEditText.value.toString()
                thisCompAmmo.componentAmmoDescription = componentAmmoDescriptionEditText.value.toString()
                thisCompAmmo.componentAmmoDODIC = componentAmmoDODICEditText.value.toString()
                thisCompAmmo.componentId = componentKey
                thisCompAmmo.weaponIdComponentAmmo = weaponKey
                update(thisCompAmmo)
                _navigateToConfirmation.value = thisCompAmmo

                Log.i("WEAPON COMP AMMO id ", " ${thisCompAmmo.componentAmmoId}")
                Log.i("WEAPON COMP_AMMO wepId ", " ${thisCompAmmo.weaponIdComponentAmmo}")
                Log.i("WEAPON COMP AMMO ty ", " ${thisCompAmmo.componentAmmoTypeID}")
                Log.i("WEAPON COMP AMMO des ", " ${thisCompAmmo.componentAmmoDescription}")
                Log.i("WEAPON COMP AMMO dod ", " ${thisCompAmmo.componentAmmoDODIC}")
                Log.i("WEAPON COMP AMMO c_id ", " ${thisCompAmmo.componentAmmoId}")

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}