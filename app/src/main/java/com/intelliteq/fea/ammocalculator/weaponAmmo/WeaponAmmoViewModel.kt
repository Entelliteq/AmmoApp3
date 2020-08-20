package com.intelliteq.fea.ammocalculator.weaponAmmo

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponAmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import com.intelliteq.fea.ammocalculator.persistence.models.WeaponAmmo
import kotlinx.coroutines.*

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

    private val _checkStatusOfInputs = MutableLiveData<Boolean>()
    val checkStatusOfInputs: LiveData<Boolean>
        get() = _checkStatusOfInputs

    private val _navigateToInputComponent = MutableLiveData<WeaponAmmo>()
    val navigateToInputComponent: LiveData<WeaponAmmo>
        get() = _navigateToInputComponent

    private val _navigateToAddAnotherAmmo = MutableLiveData<WeaponAmmo>()
    val navigateToAddAnotherAmmo: LiveData<WeaponAmmo>
        get() = _navigateToAddAnotherAmmo

    private val _navigateToConfirmation = MutableLiveData<Long>()
    val navigateToConfirmation: LiveData<Long>
        get() = _navigateToConfirmation


    init {
        initializeAmmo()
    }

    private fun initializeAmmo() {
        uiScope.launch {
            val newAmmo = WeaponAmmo()
            insert(newAmmo)
            Log.i("Initializing...", "///// $newAmmo")
            weaponAmmo.value = getAmmoFromDatabase()
            Log.i("AMMO INITIALIZED", "///// $weaponAmmo")
        }
    }

    private suspend fun getAmmoFromDatabase(): WeaponAmmo? {
        return withContext(Dispatchers.IO) {
            var weaponammo = database.getNewWeapon()
            Log.i("AMMO Returned: ", " $weaponammo")
            weaponammo
        }
    }

    private suspend fun insert(ammo: WeaponAmmo) {
        withContext(Dispatchers.IO) {
            database.insert(ammo)
            Log.i("WEAPON AMMO INSERTED  ", "/////")
        }
    }

    fun doneNavigatingToComp() {
        _navigateToInputComponent.value = null

    }

    fun doneNavigatingToAmmo() {
        _navigateToAddAnotherAmmo.value = null
        initializeAmmo()
    }

    fun onAddAnotherAmmo() {
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
                _navigateToAddAnotherAmmo.value = thisammo
                Log.i("WEAPON AMMO another ", " $thisammo")
            }
        }
    }

    fun verify(){
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
            }
        }

    }

    fun checkEditTexts(): Boolean {
        if (weaponAmmoTrainingEditText.value.isNullOrEmpty() ||
            weaponAmmoSecurityEditText.value.isNullOrEmpty() ||
            weaponAmmoSustainEditText.value.isNullOrEmpty() ||
            weaponAmmoLightEditText.value.isNullOrEmpty() ||
            weaponAmmoMediumEditText.value.isNullOrEmpty() ||
            weaponAmmoHeavyEditText.value.isNullOrEmpty())
        {
            _checkStatusOfInputs.value = false
            return false
        } else {
            _checkStatusOfInputs.value = true
            return true
        }

    }


    fun onAddComponent() {
        uiScope.launch {
            val thisammo = weaponAmmo.value ?: return@launch
            _navigateToInputComponent.value = thisammo

        }
    }

    private suspend fun update(ammo: WeaponAmmo) {
        withContext(Dispatchers.IO) {
            database.update(ammo)
            Log.i("WEAPON AMMO UPDATED", "$ammo")
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}