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

    private var thisWeapon = MutableLiveData<Weapon?>()

    val weapons = weaponDatabase.getAllWeapons()
    //var ammosList = ammoDatabase.getAll()
    val components = compoDatabase.getAll()
    val compAmmos = compAmmoDatabase.getAll()

    //read user input
    //pickers
    private val _numberOfDaysPicker = MutableLiveData<Int>()
    private val _numberOfWeaponsPicker = MutableLiveData<Int>()

    //spinners, first choice
    var _FEAidSpinner = MutableLiveData<Int>()
    var FEAidSpinner: LiveData<Int> = _FEAidSpinner



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

    private val _chosenWeapon = MutableLiveData<Weapon>()
    val chosenWeapon: LiveData<Weapon>
        get() = _chosenWeapon
    /**
     * Using the FEA number to get the weapon
     */
    fun useWeaponFea(fea: Int) {
        _FEAidSpinner.value = fea
        getChosenWeaponFEA(fea)

    }

    private val _chosenAmmoList = MutableLiveData<List<WeaponAmmo>>()
    val chosenAmmoList: LiveData<List<WeaponAmmo>>
        get() = _chosenAmmoList

    //coroutine access
    private fun getChosenWeaponFEA(fea: Int) {
        uiScope.launch {
            _chosenWeapon.value = getWeaponFromDatabaseFEA(fea)
            Log.i("Chosen Weapon?", ": ${chosenWeapon.value}")
            _chosenAmmoList.value = getChosenAmmoFromDatabaseUsingWeapon()
           // Log.i("AmmoL Weapon?", "ListAF///: ${ammosList}")

        }

    }



     private suspend fun getChosenAmmoFromDatabaseUsingWeapon() : List<WeaponAmmo>  {
        return withContext(Dispatchers.IO){
            var  ammosReturned = ammoDatabase.getAllAmmosForThisWeapon(chosenWeapon.value!!.weaponAutoId)
            Log.i("Weapon chosen:", "${chosenWeapon.value}")
            Log.i("Weapon ammo", " count: ${ammoDatabase.countAllAmmos(chosenWeapon.value!!.weaponAutoId)}")
            Log.i("Weapon ammo", " count all: ${ammoDatabase.countAll()}")
            Log.i("ALL for weapon", "${ammoDatabase.getAllAmmosForThisWeapon(chosenWeapon.value!!.weaponAutoId)}")
            Log.i("Weapon all" , "${weaponDatabase.getAllWeapons().value}")
            Log.i("Weapon this one" , "${weaponDatabase.get(chosenWeapon.value!!.weaponAutoId)}")
            Log.i("Ammos returned", "weapon ${ammosReturned}")
            ammosReturned
        }
    }

    //return from database
    private suspend fun getWeaponFromDatabaseFEA(fea: Int): Weapon? {
        return withContext(Dispatchers.IO) {
            var weaponReturned = weaponDatabase.get(fea.toLong())
            weaponReturned!!
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
            //ammosList  = ammoDatabase.getAllAmmosWithThisWeapon(chosenWeapon.weaponAutoId)
            Log.i("Type Weapon?", "${chosenWeapon.value}")
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
            //ammosList  = ammoDatabase.getAllAmmosWithThisWeapon(chosenWeapon.weaponAutoId)
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


//
//    private val _chosenAmmo = MutableLiveData<WeaponAmmo>()
//    val chosenAmmo: LiveData<WeaponAmmo>
//        get() = _chosenAmmo
//
//
//    /**
//     * Get ammo using ammo type from spinner
//     */
//    fun useAmmoType(ammoType: String) {
//        getChosenAmmoType(ammoType)
//    }
//
//    fun getChosenAmmoType(ammoType: String) {
//        uiScope.launch {
//            getChosenAmmoTypeFromDatabase(ammoType)
//            Log.i("Ammo Weapon?", "${chosenAmmo.value}")
//        }
//    }
//
//    //return from database
//    private suspend fun getChosenAmmoTypeFromDatabase(ammoType: String): WeaponAmmo {
//        return withContext(Dispatchers.IO) {
//            var ammoTypeReturned = ammoDatabase.getAmmoType(ammoType)
//            ammoTypeReturned!!
//        }
//    }
//
