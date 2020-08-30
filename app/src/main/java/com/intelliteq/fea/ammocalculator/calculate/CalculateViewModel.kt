package com.intelliteq.fea.ammocalculator.calculate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CalculateViewModel(
    val database: WeaponDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var FEA = MutableLiveData<Weapon?>()

    private val feas = database.getAll()



    //read user input
    //pickers
    private val _numberOfDaysPicker = MutableLiveData<Int>()
    private val _numberOfWeaponsPicker = MutableLiveData<Int>()

    //spinners, first choice
    val _FEAidSpinner = MutableLiveData<Int>()
    val FEAidSpinner : LiveData<Int> = _FEAidSpinner
    val weaponTypeSpinner = MutableLiveData<String>()
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

    fun getFEAentries() {
        Log.i("Weapon nums", " FEA ${feas}")
//        val arrayInt : ArrayList<Weapon> = Transformations.map(feas, {
//            arrayOf(it)
//        })
//        feas
//        for(i in arrayInt) {
//            Log.i("Weapon fea", " ${i}")
//        }
    }



    fun getWeaponNumber(number: Int) {
        _numberOfWeaponsPicker.value = number
        Log.i("#Weapons: ", " $number")
        getFEAentries()
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