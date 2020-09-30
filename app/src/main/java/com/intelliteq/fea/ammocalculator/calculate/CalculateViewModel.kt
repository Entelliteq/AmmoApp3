package com.intelliteq.fea.ammocalculator.calculate


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    val calculationsDatabase: CalculationsDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var intensity = 0
    var intensityString = MutableLiveData<String>()
    val weapons = weaponDatabase.getAllWeapons()

    //read user input
    //pickers
    private val _numberOfDaysPicker = MutableLiveData<Int>()
    val numberOfDaysPicker: LiveData<Int>
        get() = _numberOfDaysPicker

    private val _numberOfWeaponsPicker = MutableLiveData<Int>()
    val numberOfWeaponsPicker: LiveData<Int>
        get() = _numberOfWeaponsPicker


    fun combatToIntValues(combat: String) {
        intensityString.value = combat
        when (combat) {
            "Training" -> intensity = 1
            "Security" -> intensity = 2
            "Sustain" -> intensity = 3
            "Light Assault" -> intensity = 4
            "Medium Assault" -> intensity = 5
            else -> intensity = 6
        }
    }

    fun getNumberOfWeapons(number: Int) {
        _numberOfWeaponsPicker.value = number
    }

    fun getHowManyDays(number: Int) {
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
            _chosenWeapon.value = getWeaponFromDatabaseUsingFEA(fea)
            _chosenAmmoList.value = getChosenAmmoFromDatabaseUsingWeapon()
            _chosenComponentList.value = getChosenComponentFromDatabase()
        }
    }

    //Component from database
    private suspend fun getChosenComponentFromDatabase(): List<Component> {
        return withContext(Dispatchers.IO) {
            var componentsReturned =
                compoDatabase.getAllComponentsForThisWeapon(chosenWeapon.value!!.weaponAutoId)
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
    private suspend fun getWeaponFromDatabaseUsingFEA(fea: Int): Weapon? {
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
    }

    private fun getChosenComponent(compID: String) {
        uiScope.launch {
            _chosenComponent.value = getComponentFromDatabase(compID)
            _chosenComponentAmmoList.value = getComponentAmmoListFromDatabase()
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
                chosenComponent.value!!.componentId )
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
            _chosenWeapon.value = getWeaponFromDatabaseUsingType(type)
            _chosenAmmoList.value = getChosenAmmoFromDatabaseUsingWeapon()
            _chosenComponentList.value = getChosenComponentFromDatabase()
        }
    }

    //return from database
    private suspend fun getWeaponFromDatabaseUsingType(type: String): Weapon {
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
            _chosenWeapon.value = getWeaponFromDatabaseUsingDesc(ammoDesc)
            _chosenAmmoList.value = getChosenAmmoFromDatabaseUsingWeapon()
            _chosenComponentList.value = getChosenComponentFromDatabase()
        }
    }

    //return from database
    private suspend fun getWeaponFromDatabaseUsingDesc(desc: String): Weapon {
        return withContext(Dispatchers.IO) {
            var weaponReturned = weaponDatabase.getWeaponByDesc(desc)
            weaponReturned!!
        }
    }


    /**
     * Calculation
     */

    //TODO: need to add verification of all fields

//
    private var singleWeapon = MutableLiveData<SingleWeaponCalculation?>()
    private var calculation = MutableLiveData<Calculations?>()

    //
    //Navigation Mutable Live Data
    private val _navigateToAddAnotherWeaponForCalculation =
        MutableLiveData<SingleWeaponCalculation>()
    val navigateToAddAnotherWeaponForCalculation: LiveData<SingleWeaponCalculation>
        get() = _navigateToAddAnotherWeaponForCalculation

    private val _navigateToOutput = MutableLiveData<Long>()
    val navigateToOutput: LiveData<Long>
        get() = _navigateToOutput

    var visibilityDaysAndIntensity = MutableLiveData<Boolean>()
    var visibilityDaysAndIntensityOutput = MutableLiveData<Boolean>()
    var daysChosen = MutableLiveData<String>()

    fun doneNavigationToAddAnother() {
        _navigateToAddAnotherWeaponForCalculation.value = null
    }

    fun onCalculate() {
        uiScope.launch {
            _navigateToOutput.value = calculationKey


        }
    }

    //
    fun doneNavigationToOutput() {
        _navigateToOutput.value = null
    }

    init {

        if (calculationKey < 0) {
            initializeCalculation()
            visibilityDaysAndIntensity.value = true
            visibilityDaysAndIntensityOutput.value = false
        } else {
            visibilityDaysAndIntensity.value = false
            visibilityDaysAndIntensityOutput.value = true
            setTypeAndDays()
        }
        Log.i("calc OUT", "calc")
        initializeSingle()
    }

    fun initializeSingle() {
        uiScope.launch {
            val newSingle = SingleWeaponCalculation()
            insertSingleWeapon(newSingle)
            singleWeapon.value = getSingleWeaponFromDatabase()
        }
    }


    private suspend fun getSingleWeaponFromDatabase(): SingleWeaponCalculation? {
        return withContext(Dispatchers.IO) {
            var weapon = singleWeaponCalculationDatabase.getNewCalculation()
            weapon
        }
    }

    fun initializeCalculation() {
        uiScope.launch {
            val newCalculation = Calculations()
            newCalculation.numberOfDays = getDays()
            newCalculation.assaultType = intensity
            insertCalculation(newCalculation)
            calculation.value = newCalculation
            calculationKey = getCalculationFromDatabase()!!.calculationId
            newCalculation.calculationId = calculationKey
            updateCalculationsDatabase(newCalculation)
            Log.i("calc", "init $newCalculation")
        }
    }

    fun setTypeAndDays() {
        uiScope.launch {
            val calc = getCalculationFromDatabaseUsingID(calculationKey)
            intensity = calc!!.assaultType
            combatIntToString(intensity)
            daysChosen.value = calc.numberOfDays.toString()
        }
    }

    fun combatIntToString(combat: Int) : String {

        when (combat) {
            1 -> intensityString.value = "Training"
            2 -> intensityString.value = "Security"
            3 -> intensityString.value = "Sustain"
            4 -> intensityString.value = "Light Assault"
            5 -> intensityString.value = "Medium Assault"
            else -> intensityString.value = "Heavy Assault"
        }
        return intensityString.value.toString()
    }


    private suspend fun getCalculationFromDatabaseUsingID(id: Long): Calculations? {
        return withContext(Dispatchers.IO) {
            var calc = calculationsDatabase.get(id)
            calc
        }
    }

    private suspend fun getCalculationFromDatabase(): Calculations? {
        return withContext(Dispatchers.IO) {
            var calc = calculationsDatabase.getNewCalculation()
            calc
        }
    }

    fun onSubmit() {
        uiScope.launch {
            val thisCalc = calculation.value ?: return@launch
            thisCalc.assaultType = intensity
            thisCalc.numberOfDays = getDays()
            updateCalculationsDatabase(thisCalc)
            visibilityDaysAndIntensityOutput.value = true
            visibilityDaysAndIntensity.value = false
            daysChosen.value = thisCalc.numberOfDays.toString()
            Log.i("calc", "submit ${calculation.value}")
        }
    }

    fun onAddWeapon() {
        uiScope.launch {
            val thisWeapon = singleWeapon.value ?: return@launch
            thisWeapon.group_calculationID = calculationKey
            thisWeapon.numberOfWeapons = getNumberWeapons()
            thisWeapon.weaponIDCalculation = _chosenWeapon.value!!.weaponAutoId
            thisWeapon.weaponAmmoIdCalculation = chosenAmmo.value!!.ammoId
            thisWeapon.componentAmmoIdCalculation =
                chosenComponentAmmo.value!!.componentAmmoId
            updateSingle(thisWeapon)
            _navigateToAddAnotherWeaponForCalculation.value = thisWeapon
        }
    }

    //Suspend Functions

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

    private suspend fun updateCalculationsDatabase(newCalculation: Calculations) {
        withContext(Dispatchers.IO) {
            calculationsDatabase.update(newCalculation)
        }
    }

    private suspend fun updateSingleWeaponDatabase(calc: SingleWeaponCalculation) {
        withContext(Dispatchers.IO) {
            singleWeaponCalculationDatabase.update(calc)
        }
    }

     fun getDays(): Int {
        return if (numberOfDaysPicker.value == null) {
            1
        } else {
            numberOfDaysPicker.value!!
        }
    }

    private fun getNumberWeapons(): Int {
        return if (_numberOfWeaponsPicker.value == null) {
            1
        } else {
            _numberOfWeaponsPicker.value!!
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

