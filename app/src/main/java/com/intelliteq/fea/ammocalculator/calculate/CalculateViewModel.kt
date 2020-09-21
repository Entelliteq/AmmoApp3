package com.intelliteq.fea.ammocalculator.calculate

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.formulas.BasicAmmoFormula
import com.intelliteq.fea.ammocalculator.persistence.daos.*
import com.intelliteq.fea.ammocalculator.persistence.models.*
import kotlinx.coroutines.*

class CalculateViewModel(
    var calculationKey: Long,
    val weaponDatabase: WeaponDao,
    val ammoDatabase: WeaponAmmoDao,
    val compoDatabase: ComponentDao,
    val compAmmoDatabase: ComponentAmmoDao,
    val singleWeaponCalculationDatabase: SingleWeaponCalculationDao,
    val calculationsDatabase: CalculationsDao,
    val application: Application
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var intensity = 0

    // private var thisWeapon = MutableLiveData<Weapon?>()

    val weapons = weaponDatabase.getAllWeapons()

    //read user input
    //pickers
    private val _numberOfDaysPicker = MutableLiveData<Int>()
    val numberOfDaysPicker: LiveData<Int>
        get() = _numberOfDaysPicker

    private val _numberOfWeaponsPicker = MutableLiveData<Int>()
    val numberOfWeaponsPicker: LiveData<Int>
        get() = _numberOfWeaponsPicker


    fun useCombat(combat: String) {
        when (combat) {
            "Training" -> intensity = 1
            "Security" -> intensity = 2
            "Sustain" -> intensity = 3
            "Light Assault" -> intensity = 4
            "Medium Assault" -> intensity = 5
            else -> intensity = 6
        }
        Log.i("#Weapons ", "combat: $intensity")
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
    val chosenComponentList: LiveData<List<Component>>
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
        return withContext(Dispatchers.IO) {
            var componentsReturned =
                compoDatabase.getAllComponentsForThisWeapon(chosenWeapon.value!!.weaponAutoId)
            //Log.i("Weapon COMPS:", "//** $componentsReturned")
            //Log.i("Weapon ID:", " ${chosenWeapon.value!!.weaponAutoId}")
            componentsReturned
        }
    }

    //Ammo List from database
    private suspend fun getChosenAmmoFromDatabaseUsingWeapon(): List<WeaponAmmo> {
        return withContext(Dispatchers.IO) {
            var ammosReturned =
                ammoDatabase.getAllAmmosForThisWeapon(chosenWeapon.value!!.weaponAutoId)
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

    fun useAmmo(ammoId: String) {
        getChosenAmmo(ammoId)
    }

    fun getChosenAmmo(ammoId: String) {
        uiScope.launch {
            _chosenAmmo.value = getChosenAmmoFromDatabase(ammoId)
            Log.i("Comp2:", "//**A ${chosenAmmo.value!!.ammoType}")
        }
    }

    private suspend fun getChosenAmmoFromDatabase(ammoId: String): WeaponAmmo? {
        return withContext(Dispatchers.IO) {
            var ammoReturned = ammoDatabase.getAmmoType(ammoId)
            ammoReturned
        }
    }

    //chosen comp ammo
    fun useComponentAmmo(compID: String) {
        getChosenComponentAmmo(compID)
    }

    fun getChosenComponentAmmo(compID: String) {
        uiScope.launch {
            _chosenComponentAmmo.value = getCompAmmoFromDatabase(compID)
            Log.i("Comp2:", "//**CA ${chosenComponentAmmo.value!!.componentAmmoTypeID}")
        }
    }

    private suspend fun getCompAmmoFromDatabase(compID: String): ComponentAmmo? {
        return withContext(Dispatchers.IO) {
            var compAmmoReturned = compAmmoDatabase.getUsingIDString(compID)
            compAmmoReturned
        }
    }

    private val _chosenComponentAmmoList = MutableLiveData<List<ComponentAmmo>>()
    val chosenComponentAmmoList: LiveData<List<ComponentAmmo>>
        get() = _chosenComponentAmmoList

    private val _chosenComponent = MutableLiveData<Component>()
    val chosenComponent: LiveData<Component>
        get() = _chosenComponent

    private val _chosenAmmo = MutableLiveData<WeaponAmmo>()
    val chosenAmmo: LiveData<WeaponAmmo>
        get() = _chosenAmmo

    private val _chosenComponentAmmo = MutableLiveData<ComponentAmmo>()
    val chosenComponentAmmo: LiveData<ComponentAmmo>
        get() = _chosenComponentAmmo

    fun useComponent(compID: String) {
        getChosenComponent(compID)
        if (chosenComponent.value != null)
            Log.i("Component:", "// ${chosenComponent.value!!.componentTypeID}")
        // getChosenComponentAmmoList(chosenComponent.value!!)
    }

    private fun getChosenComponent(compID: String) {
        uiScope.launch {
            _chosenComponent.value = getComponentFromDatabase(compID)
            _chosenComponentAmmoList.value = getComponentAmmoListFromDatabase()
            Log.i("Comp2", "//**Comp: ${chosenComponent.value!!.componentTypeID}")
            // _chosenComponentAmmoList.value =
        }
    }

    private suspend fun getComponentFromDatabase(id: String): Component {
        return withContext(Dispatchers.IO) {
            var componentReturned = compoDatabase.getWithType(id)
            componentReturned
        }
    }


    //ComponentAmmo from database
    private suspend fun getComponentAmmoListFromDatabase(): List<ComponentAmmo> {
        return withContext(Dispatchers.IO) {
            var componentAmmosReturned = compAmmoDatabase.getComponentAmmosForThisComponent(
                chosenComponent.value!!.componentId
            )
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
            // Log.i("Desc Weapon?", "${chosenWeapon.value}")
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
     * Calculation
     */

    //TODO: need to add verification of all fields


    private var singleWeapon = MutableLiveData<SingleWeaponCalculation?>()
    private var calculation = MutableLiveData<Calculations?>()

    //Navigation Mutable Live Data
    private val _navigateToInputAnotherAmmoScreen = MutableLiveData<SingleWeaponCalculation>()
    val navigateToInputAnotherAmmoScreen: LiveData<SingleWeaponCalculation>
        get() = _navigateToInputAnotherAmmoScreen

    private val _navigateToOutputScreen = MutableLiveData<Long>()
    val navigateToOutputScreen: LiveData<Long>
        get() = _navigateToOutputScreen

    fun doneNavigation() {
        // _navigateToInputAnotherAmmoScreen.value = null
        //initializeSignleWeapon()
        Log.i("Called calc", "")
    }

    fun doneNavigationToOutput() {
        _navigateToOutputScreen.value = null
    }

    init {
        if (calculationKey < 1) {
            initializeCalculation()
            Log.i("Init calc ", " $calculationKey")
        }
        initializeSignleWeapon()
        Log.i("Init calc weap:", " $calculationKey")
    }


    fun initializeSignleWeapon() {
        uiScope.launch {
            val newWeapon = SingleWeaponCalculation()
            Log.i("NEW Single Calc", "**^^${calculationKey}")
            //newWeapon.group_calculationID = calculationKey
            insertSingleWeapon(newWeapon)
            singleWeapon.value = getSingleWeaponFromDatabase()
            Log.i("NEW Single Calc", "**^^${singleWeapon.value}")
            Log.i("NEW Single Calc", "**^^${calculationKey}")
        }

    }

    private suspend fun getSingleWeaponFromDatabase(): SingleWeaponCalculation? {
        return withContext(Dispatchers.IO) {
            var weapon = singleWeaponCalculationDatabase.getNewCalculation()
            Log.i("NEW Weapon Calc", "*^${weapon}")
            weapon
        }
    }


    fun initializeCalculation() {
        uiScope.launch {
            val newCalculation = Calculations()
            newCalculation.numberOfDays = getDays()
            newCalculation.assaultType = intensity
            insertCalculation(newCalculation)
            calculationKey = getCalculationFromDatabase()!!.calculationId
            Log.i("NEW Calc Key:", "!!*${calculationKey}")
        }
    }

    private suspend fun getCalculationFromDatabase(): Calculations? {
        return withContext(Dispatchers.IO) {
            var calc = calculationsDatabase.getNewCalculation()
            calc
        }
    }

    fun onCalculate() {
        uiScope.launch {
            _navigateToOutputScreen.value = calculationKey
            Log.i("Called Here", "am i here $calculationKey")
        }
    }

    fun onAddWeapon() {
        uiScope.launch {
            val thisweapon = singleWeapon.value ?: return@launch
            thisweapon.weaponAmmoIdCalculation = chosenAmmo.value!!.ammoId
            thisweapon.componentAmmoIdCalculation =
                chosenComponentAmmo.value!!.componentAmmoId
            thisweapon.numberOfWeapons = getNumberWeapons()
            thisweapon.weaponIDCalculation = chosenWeapon.value!!.weaponAutoId
            thisweapon.group_calculationID = calculationKey
            updateSingle(thisweapon)
            _navigateToInputAnotherAmmoScreen.value = thisweapon
            Log.i("ADD Weapon single", "**${singleWeapon.value}")
            Log.i("ADD this weap", "**${thisweapon}")
            Log.i("ADD calc key", " key:**${calculationKey}")
            // Log.i("NEW Calc Key", "**${thisweapon.calculationId}")

        }
    }

    private suspend fun updateSingle(thisweapon: SingleWeaponCalculation) {
        withContext(Dispatchers.IO) {
            singleWeaponCalculationDatabase.update(thisweapon)
        }
    }


    private suspend fun insertSingleWeapon(calc: SingleWeaponCalculation) {
        withContext(Dispatchers.IO) {
            singleWeaponCalculationDatabase.insert(calc)
        }

    }

    private suspend fun insertCalculation(newCalculation: Calculations) {
        withContext(Dispatchers.IO) {
            calculationsDatabase.insert(newCalculation)
        }
    }

    //
//    fun initializeNewWeapon() {
//        uiScope.launch {
//            val newWeaponCalculation = SingleWeaponCalculation()
//            insert(newWeaponCalculation)
//            singleCalculation.value = getSingleCalculationFromDatabase()
//
//        }
//    }
//


    private suspend fun updateCalculationsDatabase(newCalculation: Calculations) {
        withContext(Dispatchers.IO) {
            calculationsDatabase.update(newCalculation)
        }
    }

//    private suspend fun update(calc: SingleWeaponCalculation) {
//        withContext(Dispatchers.IO) {
//            singleWeaponCalculationDatabase.update(calc)
//        }
//    }

    private fun getDays(): Int {
        if (numberOfDaysPicker.value == null) {
            return 0
        } else {
            return numberOfDaysPicker.value!!
        }
    }

    private fun getNumberWeapons(): Int {
        if (_numberOfWeaponsPicker.value == null) {
            return 0
        } else {
            return _numberOfWeaponsPicker.value!!
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

