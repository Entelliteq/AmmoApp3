package com.intelliteq.fea.ammocalculator.editWeaponInput


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import kotlinx.coroutines.*

class EditWeaponViewModel(
    private val weaponKey: Long,
    private val componentDao: ComponentDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //reading the user input
    val weaponDescriptionEditText = MutableLiveData<String>()
    val weaponTypeEditText = MutableLiveData<String>()

    private var componentOld = MutableLiveData<Component?>()
    var weaponDescriptionHint = MutableLiveData<String>()
    var weaponTypeHint = MutableLiveData<String>()

    init {
        populateHints()
    }

    private fun populateHints() {
        uiScope.launch {
            weaponDescriptionHint.value = getDescriptionFromDatabase()
            weaponTypeHint.value = getTypeFromDatabase()
            componentOld.value = getComponentFromDatabase()

        }
    }
    private suspend fun getComponentFromDatabase() : Component {
        return withContext(Dispatchers.IO) {
            val comp = componentDao.getWeaponNotNull(weaponKey)
            comp
        }
    }

    private suspend fun getDescriptionFromDatabase() : String {
        return withContext(Dispatchers.IO) {
            val hint = componentDao.getWeaponNotNull(weaponKey).componentDescription
            hint
        }
    }

    private suspend fun getTypeFromDatabase() : String {
        return withContext(Dispatchers.IO) {
            val hint = componentDao.getWeaponNotNull(weaponKey).componentTypeId
            hint
        }
    }

    fun updateDescription(desc: String) {
        uiScope.launch {
            val thisWeapon = componentOld.value?: return@launch
            thisWeapon.componentDescription = desc
            update(thisWeapon)
          //  Log.i("edit4", "$thisWeapon")
        }
    }


    fun updateType(type: String) {
        uiScope.launch {
            val thisWeapon = componentOld.value?: return@launch
            thisWeapon.componentTypeId = type
            update(thisWeapon)
          //  Log.i("edit4", "$thisWeapon")
        }
    }

    private suspend fun update(thisWeapon: Component) {
        withContext(Dispatchers.IO) {
            componentDao.update(thisWeapon)
           // Log.i("edit4", "$thisWeapon")
        }
    }


}