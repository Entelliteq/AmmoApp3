package com.intelliteq.fea.ammocalculator.calculate

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentAmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponAmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.ComponentAmmo
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import com.intelliteq.fea.ammocalculator.persistence.models.WeaponAmmo
import kotlinx.coroutines.*

class CalculateViewModel(
    val weaponDatabase: WeaponDao,
    val ammoDatabase: WeaponAmmoDao,
    val compoDatabase: ComponentDao,
    val compAmmoDatabase: ComponentAmmoDao,
    val application: Application
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

   // private var thisWeapon = MutableLiveData<Weapon?>()

    val weapons = weaponDatabase.getAllWeapons()

    //read user input
    //pickers
    private val _numberOfDaysPicker = MutableLiveData<Int>()
    private val _numberOfWeaponsPicker = MutableLiveData<Int>()


    //Navigation Mutable Live Data
    private val _navigateToCalculateScreen = MutableLiveData<Component>()
    val navigateToInputComponentAmmo: LiveData<Component>
        get() = _navigateToCalculateScreen


    fun useCombat(combat: String) {
        Log.i("#Weapons: ", " combat $combat")
    }

    fun getWeaponNumber(number: Int) {
        _numberOfWeaponsPicker.value = number
        Log.i("#Weapons: ", " ${_numberOfWeaponsPicker.value}")

    }

    fun getDayNumber(number: Int) {
        _numberOfDaysPicker.value = number
        Log.i("#Weapon Days: ", " $number")
    }


    /**
     * Using the FEA number to get the weapon, ammo and component
     */

    private val _chosenAmmoList = MutableLiveData<List<WeaponAmmo>>()
    val chosenAmmoList: LiveData<List<WeaponAmmo>>
        get() = _chosenAmmoList

    private val _chosenWeapon = MutableLiveData<Weapon>()
    val chosenWeapon: LiveData<Weapon>
        get() = _chosenWeapon

    private val _chosenComponentList = MutableLiveData<List<Component>>()
    val  chosenComponentList: LiveData<List<Component>>
        get() = _chosenComponentList

    fun useWeaponFea(fea: Int) {
        getChosenWeaponFEA(fea)
    }

    //coroutine access
    private fun getChosenWeaponFEA(fea: Int) {
        uiScope.launch {
            _chosenWeapon.value = getWeaponFromDatabaseFEA(fea)
            _chosenAmmoList.value = getChosenAmmoFromDatabaseUsingWeapon()
            _chosenComponentList.value = getChosenComponentFromDatabase()
        }

    }

    //Component from database
    private suspend fun getChosenComponentFromDatabase(): List<Component> {
        return withContext(Dispatchers.IO){
            var componentsReturned = compoDatabase.getAllComponentsForThisWeapon(chosenWeapon.value!!.weaponAutoId)
            //Log.i("Weapon COMPS:", "//** $componentsReturned")
            //Log.i("Weapon ID:", " ${chosenWeapon.value!!.weaponAutoId}")
            componentsReturned
        }
    }

    //Ammo from database
     private suspend fun getChosenAmmoFromDatabaseUsingWeapon() : List<WeaponAmmo>  {
        return withContext(Dispatchers.IO){
            var  ammosReturned = ammoDatabase.getAllAmmosForThisWeapon(chosenWeapon.value!!.weaponAutoId)
            ammosReturned
        }
    }

    //Weapon from database
    private suspend fun getWeaponFromDatabaseFEA(fea: Int): Weapon? {
        return withContext(Dispatchers.IO) {
            var weaponReturned = weaponDatabase.get(fea.toLong())
            weaponReturned!!
        }
    }

    /**
     * Chosen Component for ComponentAmmos
     */

    private val _chosenComponentAmmoList = MutableLiveData<List<ComponentAmmo>>()
    val  chosenComponentAmmoList: LiveData<List<ComponentAmmo>>
        get() = _chosenComponentAmmoList

    private val _chosenComponent = MutableLiveData<Component>()
    val chosenComponent: LiveData<Component>
        get() = _chosenComponent

    fun useComponent(compID: String) {
        getChosenComponent(compID)
       // getChosenComponentAmmoList(chosenComponent.value!!)
    }

    private fun getChosenComponent(compID: String) {
        uiScope.launch {
            _chosenComponent.value = getComponentFromDatabase(compID)
            _chosenComponentAmmoList.value = getComponentAmmoListFromDatabase()
           // Log.i("Weapon comp", "//**DB: ${chosenComponent.value}")
           // _chosenComponentAmmoList.value =
        }
    }

    private suspend fun getComponentFromDatabase(id: String) : Component {
        return withContext(Dispatchers.IO) {
            var componentReturned = compoDatabase.getWithType(id)
            componentReturned
        }
    }



    //ComponentAmmo from database
    private suspend fun getComponentAmmoListFromDatabase() : List<ComponentAmmo> {
        return withContext(Dispatchers.IO) {
            var componentAmmosReturned = compAmmoDatabase.getComponentAmmosForThisComponent(chosenComponent.value!!.componentId)
            componentAmmosReturned
        }
    }



    /**
     * Use weapon type to get weapon
     */
    fun useWeaponType(ammoType: String) {
        getChosenWeaponType(ammoType)
    }

    //coroutine access
    private fun getChosenWeaponType(type: String) {
        uiScope.launch {
             getWeaponFromDatabaseType(type)
        }
    }

    //return from database
    private suspend fun getWeaponFromDatabaseType(type: String): Weapon {
        return withContext(Dispatchers.IO) {
            var weaponReturned = weaponDatabase.getWeaponByType(type)
            weaponReturned!!
        }
    }



    /**
     * Use weapon description to get weapon
     */
    fun useWeaponDesc(ammoDesc: String) {
        getChosenWeaponDesc(ammoDesc)
    }

    //coroutine access
    private fun getChosenWeaponDesc(ammoDesc: String) {
        uiScope.launch {
            getWeaponFromDatabaseDesc(ammoDesc)
            Log.i("Desc Weapon?", "${chosenWeapon.value}")
        }
    }

    //return from database
    private suspend fun getWeaponFromDatabaseDesc(desc: String): Weapon {
        return withContext(Dispatchers.IO) {
            var weaponReturned = weaponDatabase.getWeaponByDesc(desc)
            weaponReturned!!
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

