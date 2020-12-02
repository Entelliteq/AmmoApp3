package com.intelliteq.fea.ammocalculator.editAmmoInput

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import kotlinx.coroutines.*

class EditAmmoViewModel(
    private val ammoKey: Long, //think I need the componentKey somehow
    private val ammoDao: AmmoDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //reading the user input
    val ammoDescriptionEditText = MutableLiveData<String>()
    val ammoTypeEditText = MutableLiveData<String>()

    var ammoOld = MutableLiveData<Ammo?>()
    var ammoDescriptionHint = MutableLiveData<String>()
    var ammoTypeHint = MutableLiveData<String>()
    var ammoTrainingHint = MutableLiveData<String>()
    var ammoSustainHint = MutableLiveData<String>()
    var ammoSecurityHint = MutableLiveData<String>()
    var ammoLightHint = MutableLiveData<String>()
    var ammoMediumHint = MutableLiveData<String>()
    var ammoHeavyHint = MutableLiveData<String>()

    init {
        populateHints()
    }


    private fun populateHints() {
        uiScope.launch {

            ammoDescriptionHint.value = getDescriptionFromDatabase()
            ammoTypeHint.value = getTypeFromDatabase()
            ammoOld.value = getAmmoFromDatabase()
            ammoHeavyHint.value = getHeavyFromDatabase()
            ammoLightHint.value = getLightFromDatabase()
            ammoMediumHint.value = getMediumFromDatabse()
            ammoSecurityHint.value = getSecurityFromDatabase()
            ammoSustainHint.value = getSustainFromDatabase()
            ammoTrainingHint.value = getTrainingFromDatabase()

        }
    }

    private suspend fun getLightFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).lightAssaultRate.toString()
            value
        }
    }


    private suspend fun getMediumFromDatabse(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).mediumAssaultRate.toString()
            value
        }
    }


    private suspend fun getSecurityFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).securityRate.toString()
            value
        }
    }


    private suspend fun getHeavyFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).heavyAssaultRate.toString()
            value
        }
    }

    private suspend fun getSustainFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).sustainRate.toString()
            value
        }
    }

    private suspend fun getTrainingFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            val value = ammoDao.get(ammoKey).trainingRate.toString()
            value
        }
    }

    private suspend fun getAmmoFromDatabase(): Ammo {
        return withContext(Dispatchers.IO) {
            val comp = ammoDao.get(ammoKey)
            comp
        }
    }

    private suspend fun getDescriptionFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            var hint = ammoDao.get(ammoKey).ammoDescription
            hint
        }.toString()
    }

    private suspend fun getTypeFromDatabase(): String {
        return withContext(Dispatchers.IO) {
            var hint = ammoDao.get(ammoKey).ammoDODIC.toString()
            hint
        }
    }

    private suspend fun update(ammo: Ammo) {
        withContext(Dispatchers.IO) {
            ammoDao.update(ammo)
            // Log.i("edit4", "$thisWeapon")
        }
    }

    fun updateDodic(dodic: String) {
        uiScope.launch {
            var thisammo = ammoOld.value?: return@launch
            thisammo.ammoDODIC = dodic
            update(thisammo)
            //  Log.i("edit4", "$thisWeapon")
        }
    }

    fun updateSustain(desc: Int) {
        uiScope.launch {
            var thisammo = ammoOld.value?: return@launch
            thisammo.sustainRate = desc
            update(thisammo)
            //  Log.i("edit4", "$thisWeapon")
        }
    }

    fun updateSecurity(desc: Int) {
        uiScope.launch {
            var thisammo = ammoOld.value?: return@launch
            thisammo.securityRate = desc
            update(thisammo)
            //  Log.i("edit4", "$thisWeapon")
        }
    }

    fun updateTraining(desc: Int) {
        uiScope.launch {
            var thisammo = ammoOld.value?: return@launch
            thisammo.trainingRate = desc
            update(thisammo)
            //  Log.i("edit4", "$thisWeapon")
        }
    }

    fun updateLight(desc: Int) {
        uiScope.launch {
            var thisammo = ammoOld.value?: return@launch
            thisammo.lightAssaultRate = desc
            update(thisammo)
            //  Log.i("edit4", "$thisWeapon")
        }
    }

    fun updateMedium(desc: Int) {
        uiScope.launch {
            var thisammo = ammoOld.value?: return@launch
            thisammo.mediumAssaultRate = desc
            update(thisammo)
            //  Log.i("edit4", "$thisWeapon")
        }
    }

    fun updateHeavy(desc: Int) {
        uiScope.launch {
            var thisammo = ammoOld.value?: return@launch
            thisammo.heavyAssaultRate = desc
            update(thisammo)
            //  Log.i("edit4", "$thisWeapon")
        }
    }

    fun updateDesc(desc: String) {
        uiScope.launch {
            var thisammo = ammoOld.value?: return@launch
            thisammo.ammoDescription = desc
            update(thisammo)
            //  Log.i("edit4", "$thisWeapon")
        }
    }


}