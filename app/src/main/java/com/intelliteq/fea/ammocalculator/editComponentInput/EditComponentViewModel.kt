package com.intelliteq.fea.ammocalculator.editComponentInput

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import kotlinx.coroutines.*

class EditComponentViewModel (
    private val componentKey: Long, //think I need the componentKey somehow
    private val componentDao: ComponentDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //reading the user input
    val componentDescriptionEditText = MutableLiveData<String>()
    val componentTypeEditText = MutableLiveData<String>()

    private var componentOld = MutableLiveData<Component?>()
    var componentDescriptionHint = MutableLiveData<String>()
    var componentTypeHint = MutableLiveData<String>()

    init {
        populateHints()
    }

    private fun populateHints() {
        uiScope.launch {
            componentDescriptionHint.value = getDescriptionFromDatabase()
            componentTypeHint.value = getTypeFromDatabase()
            componentOld.value = getComponentFromDatabase()
        }
    }
    
    //suspend functions
    private suspend fun getComponentFromDatabase() : Component {
        return withContext(Dispatchers.IO) {
            val comp = componentDao.get(componentKey)
            comp
        }
    }

    private suspend fun getDescriptionFromDatabase() : String {
        return withContext(Dispatchers.IO) {
            val hint = componentDao.get(componentKey).componentDescription
            hint
        }
    }

    private suspend fun getTypeFromDatabase() : String {
        return withContext(Dispatchers.IO) {
            val hint = componentDao.get(componentKey).componentTypeId
            hint
        }
    }

    //update functions
    fun updateDescription(desc: String) {
        uiScope.launch {
            val thisWeapon = componentOld.value?: return@launch
            thisWeapon.componentDescription = desc
            update(thisWeapon)
        }
    }


    fun updateType(type: String) {
        uiScope.launch {
            val thisWeapon = componentOld.value?: return@launch
            thisWeapon.componentTypeId = type
            update(thisWeapon)
        }
    }

    private suspend fun update(thisWeapon: Component) {
        withContext(Dispatchers.IO) {
            componentDao.update(thisWeapon)
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