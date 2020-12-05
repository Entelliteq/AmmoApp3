package com.intelliteq.fea.ammocalculator.validation


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.AmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import kotlinx.coroutines.*

class ValidationViewModel(
    private val weaponKey: Long,
    private val componentDatabase: ComponentDao,
    ammoDatabase: AmmoDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var weapon = MutableLiveData<Component?>()
    val ammosWeapon = ammoDatabase.getAllWeaponAmmos(weaponKey)
    val ammosComp = ammoDatabase.getAllComponentAmmos(weaponKey)
    val components = componentDatabase.getAllComponents(weaponKey)

   // var weaponType = weapon.value!!.componentTypeId

    init {
        initializeWeapon()
       // Log.i("Verify1", "${weapon.value}")
    }

    private fun initializeWeapon() {
        uiScope.launch {
            weapon.value = getWeaponFromDatabase()
           // Log.i("Verify2", "${weapon.value}")

        }

    }

    private suspend fun getWeaponFromDatabase() : Component? {
        return withContext(Dispatchers.IO) {
            val weapon = componentDatabase.getWeapon(weaponKey)
         //   Log.i("VERIFY 3" , "$weapon")
            weapon
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