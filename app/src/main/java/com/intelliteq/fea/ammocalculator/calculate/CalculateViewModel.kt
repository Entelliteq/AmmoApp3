package com.intelliteq.fea.ammocalculator.calculate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    //read user input
    //spinners and pickers
    val FEAidSpinner = MutableLiveData<Int>()
    val weaponTypeSpinner = MutableLiveData<String>()
    val weaponDescriptionSpinner = MutableLiveData<String>()
    val numberOfWeaponsPicker = MutableLiveData<Int>()
    val ammoTypeSpinner = MutableLiveData<String>()
    val numberOfDaysPicker = MutableLiveData<Int>()
    val combatTypeSpinner = MutableLiveData<String>()
    val componentTypeSpinner = MutableLiveData<String>()
    val componentAmmoSpinner = MutableLiveData<String>()

    //Navigation Mutable Live Data
    private val _navigateToCalculateScreen = MutableLiveData<Component>()
    val navigateToInputComponentAmmo: LiveData<Component>
        get() = _navigateToCalculateScreen





}