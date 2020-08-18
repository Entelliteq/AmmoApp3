package com.intelliteq.fea.ammocalculator.component

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import kotlinx.coroutines.*

class ComponentViewModel (
    private val weaponKey: Long = 0L,
    val database: ComponentDao
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob )

    private var component = MutableLiveData<Component?>()

    private val _navigateToInputComponentAmmo = MutableLiveData<Component>()
    val navigateToInputComponentAmmo: LiveData<Component>
        get() = _navigateToInputComponentAmmo

    private val _navigateToAnotherComponent = MutableLiveData<Component>()
    val navigateToAnotherComponent: LiveData<Component>
        get() = _navigateToAnotherComponent

    init {
        initializeComponent()
    }

    private fun initializeComponent() {
        uiScope.launch {
            val newComponent = Component()
            insert(newComponent)
        }
    }

    private suspend fun insert(component: Component) {
        withContext(Dispatchers.IO) {
            database.insert(component)
        }
    }

    fun addAnotherComponent() {
        _navigateToAnotherComponent.value = null
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
