package com.intelliteq.fea.ammocalculator.calculate

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import kotlinx.coroutines.*

class CalculateViewModel(
    val database: WeaponDao,
    val application: Application
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var thisWeapon = MutableLiveData<Weapon?>()

    val weapons = database.getAllWeapons()

    //read user input
    //pickers
    private val _numberOfDaysPicker = MutableLiveData<Int>()
    private val _numberOfWeaponsPicker = MutableLiveData<Int>()

    //spinners, first choice
    var _FEAidSpinner = MutableLiveData<Int>()
    var FEAidSpinner: LiveData<Int> = _FEAidSpinner
    var FEASpinnerValues: MutableList<Int> = mutableListOf()

    private val _feaPick = MutableLiveData<Int>()
    val feaPick: LiveData<Int>
        get() = _feaPick

    fun useFea(fea: Int) {
        Log.i("FEA Weapon??" , "$fea")
    }

    val _weaponTypeSpinner = MutableLiveData<Spinner>()
    val weaponTypeSpinner : LiveData<Spinner> = _weaponTypeSpinner
    var weaponTypeSpinnerValues: MutableList<String> = mutableListOf()

    val weaponDescriptionSpinner = MutableLiveData<String>()


    //spinner, second choice
    val ammoTypeSpinner = MutableLiveData<String>()
    val combatTypeSpinner = MutableLiveData<String>()
    val componentTypeSpinner = MutableLiveData<String>()
    val componentAmmoSpinner = MutableLiveData<String>()

    //Navigation Mutable Live Data
    private val _navigateToCalculateScreen = MutableLiveData<Component>()
    val navigateToInputComponentAmmo: LiveData<Component>
        get() = _navigateToCalculateScreen

    init {

        Log.i("WEapon FEA:" , "$FEAidSpinner")
    }



//
//    private fun initializeFEASpinner() {
//        uiScope.launch {
//            FEASpinnerValues = getWeaponFEAFromDatabase()
//           // FEASpinnerValues = FEAidSpinner.value
//            Log.i("Weapon spinner: ", "FEA: ${FEASpinnerValues}")
//            sendFEAValues()
//
//        }
//
//    }

//    private fun initializeWeaponTypeSpinner() {
//        uiScope.launch {
//            weaponTypeSpinnerValues = getWeaponTypeFromDatabase()
//            Log.i("Weapon spinner: ", "FEA: ${weaponTypeSpinnerValues}")
//        }
//
//    }

//    private suspend fun getWeaponFEAFromDatabase(): MutableList<Int> {
//        return withContext(Dispatchers.IO) {
//            var list: MutableList<Int> = mutableListOf()
//            var weaponlist = database.getAllWeapons()
//            weaponlist.forEach {
//                list.add(it.FEA_id)
//            }
//            list
//        }
//
//    }



//    private suspend fun getWeaponTypeFromDatabase(): MutableList<String> {
//        return withContext(Dispatchers.IO) {
//            var list: MutableList<String> = mutableListOf()
//            var weaponlist = database.getAllWeapons()
//            weaponlist.forEach {
//                list.add(it.weaponTypeID)
//            }
//            list
//        }
//
//    }

    fun getWeaponNumber(number: Int) {
        _numberOfWeaponsPicker.value = number
        Log.i("#Weapons: ", " $number")

    }

    fun sendFEAValues() : MutableList<Int> {
        Log.i("GEtting Weapon" , "FEA list into spinner ${FEASpinnerValues}")
        return FEASpinnerValues
    }

    fun getDayNumber(number: Int) {
        _numberOfDaysPicker.value = number
        Log.i("#Weapon Days: ", " $number")
    }


    /**
     * Cancelling all jobs
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}