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
    private val ammoDatabase: AmmoDao,
    private val compoDatabase: ComponentDao,
    private val perWeaponCalculationDatabase: PerWeaponCalculationDao,
    private val calculationDatabase: CalculationDao
) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var noComponentAmmo = false
    val weapons = compoDatabase.getAllWeapons()
    // val componentWeapon = compoDatabase.getAllWeapons()     //TODO: not sure here, maybe delete or update


    /**
     * LiveData
     */

    //intensity
    //private var assaultIntensity = ""
    var assaultIntensityString = MutableLiveData<String>()

    //pickers
    private val _numberOfDaysPicker = MutableLiveData<Int>()
    private val numberOfDaysPicker: LiveData<Int>
        get() = _numberOfDaysPicker

    private val _numberOfWeaponsPicker = MutableLiveData<Int>()

    //ammo
    private val _chosenAmmoList = MutableLiveData<List<Ammo>>()
    val chosenAmmoList: LiveData<List<Ammo>>
        get() = _chosenAmmoList

    private val _chosenAmmo = MutableLiveData<Ammo>()
    private val chosenAmmo: LiveData<Ammo>
        get() = _chosenAmmo

    //weapon
    private val _chosenWeapon = MutableLiveData<Component>()
    private val chosenWeapon: LiveData<Component>
        get() = _chosenWeapon

    //component
    private val _chosenComponentList = MutableLiveData<List<Component>>()
    val chosenComponentList: LiveData<List<Component>>
        get() = _chosenComponentList

    private val _chosenComponent = MutableLiveData<Component>()
    private val chosenComponent: LiveData<Component>
        get() = _chosenComponent

    private val _chosenComponentAmmo = MutableLiveData<Ammo>()
    private val chosenComponentAmmo: LiveData<Ammo>
        get() = _chosenComponentAmmo


    private val _chosenComponentAmmoList = MutableLiveData<List<Ammo>>()
    val chosenComponentAmmoList: LiveData<List<Ammo>>
        get() = _chosenComponentAmmoList

    //calculations
    private var singleWeapon = MutableLiveData<PerWeaponCalculation?>()
    private var calculation = MutableLiveData<Calculation?>()

    //navigation
    private val _navigateToAddAnotherWeaponForCalculation =
        MutableLiveData<PerWeaponCalculation>()
    val navigateToAddAnotherWeaponForCalculation: LiveData<PerWeaponCalculation>
        get() = _navigateToAddAnotherWeaponForCalculation

    private val _navigateToOutput = MutableLiveData<Calculation>()
    val navigateToOutput: LiveData<Calculation>
        get() = _navigateToOutput

    //visibility
    var visibilityDaysAndIntensity = MutableLiveData<Boolean>()
    var visibilityDaysAndIntensityOutput = MutableLiveData<Boolean>()
    var daysChosen = MutableLiveData<String>()


    /**
     * Assault Intensity
     * Which intensity is picked by user, then need to get values
     * from database based on the chosen assault intensity
     */
    fun assaultIntensityStringToIntValues(combat: String) {
        if (combat == "none") {
            assaultIntensityString.value = "Training"
        } else {
            assaultIntensityString.value = combat
        }

    }

    /**
     * Number of Weapons and Number of Days
     */
    fun getNumberOfWeapons(number: Int) {
        _numberOfWeaponsPicker.value = number
    }

    fun getHowManyDays(number: Int) {
        _numberOfDaysPicker.value = number
        //  Log.i("#Weapon Days: ", " $number")
    }

    /**
     * Using the FEA number to get the weapon, ammo and component
     */

    fun useWeaponFea(fea: Int) {
        getChosenWeaponFEA(fea)
    }

    //coroutine access
    private fun getChosenWeaponFEA(fea: Int) {
        uiScope.launch {
            _chosenWeapon.value = getWeaponFromDatabaseUsingFEA(fea)
            _chosenAmmoList.value = getChosenAmmoFromDatabaseUsingWeapon()
            _chosenComponentList.value = getChosenComponentFromDatabase()
            if (chosenComponentList.value!!.isEmpty()) {
                useComponent("")
            }
        }
    }

    //Component List from database
    private suspend fun getChosenComponentFromDatabase(): List<Component> {
        return withContext(Dispatchers.IO) {
            val componentsReturned =
                compoDatabase.getAllComponentsForThisWeapon(chosenWeapon.value!!.weaponId)
            componentsReturned
        }
    }

    //Ammo List from database
    private suspend fun getChosenAmmoFromDatabaseUsingWeapon(): List<Ammo> {
        return withContext(Dispatchers.IO) {
            var ammoReturned = mutableListOf<Ammo>()
            ammoReturned.addAll(ammoDatabase.getDefaultAmmoForThisWeapon(chosenWeapon.value?.weaponId))
            ammoReturned.addAll(ammoDatabase.getAllAmmosForThisWeaponExceptDefault(
                chosenWeapon.value!!.weaponId))
            Log.i("BOX5", "$ammoReturned")
            ammoReturned
        }
    }

    //Weapon from database
    private suspend fun getWeaponFromDatabaseUsingFEA(fea: Int): Component? {
        return withContext(Dispatchers.IO) {
            val weaponReturned = compoDatabase.getUsingFEA(fea.toLong())
            weaponReturned
        }
    }

    /**
     * Chosen Component for ComponentAmmos
     */
    fun useAmmo(ammoId: String) {
        getChosenAmmo(ammoId)
    }

    private fun getChosenAmmo(ammoId: String) {
        uiScope.launch {
            _chosenAmmo.value = getChosenAmmoFromDatabase(ammoId, chosenWeapon.value!!.weaponId)
        }
    }

    private suspend fun getChosenAmmoFromDatabase(ammoId: String, weaponId: Long): Ammo? {
        return withContext(Dispatchers.IO) {
            val ammoReturned = ammoDatabase.getAmmoType(ammoId, weaponId)
            ammoReturned
        }
    }

    //chosen comp ammo
    fun useComponentAmmo(compID: String) {
        getChosenComponentAmmo(compID)
    }

    private fun getChosenComponentAmmo(compID: String) {
        uiScope.launch {
            _chosenComponentAmmo.value = getCompAmmoFromDatabase(compID)
            Log.i("crash5", "${_chosenComponentAmmo.value}")

        }
    }

    private suspend fun getCompAmmoFromDatabase(compID: String): Ammo? {
        return withContext(Dispatchers.IO) {
            val compAmmoReturned = ammoDatabase.getUsingType(compID, chosenWeapon.value!!.weaponId)
            compAmmoReturned
        }
    }


    fun useComponent(compID: String) {
        getChosenComponent(compID)

    }


    private fun getChosenComponent(compID: String) {
        uiScope.launch {
            _chosenComponent.value = getComponentFromDatabase(compID)
            _chosenComponentAmmoList.value = getComponentAmmoListFromDatabase()
            noComponentAmmo = _chosenComponentAmmoList.value.isNullOrEmpty()
        }
    }

    private suspend fun getComponentFromDatabase(id: String): Component {
        return withContext(Dispatchers.IO) {
            val componentReturned = compoDatabase.getUsingType(id)
            //Log.i("Called" ,"from db: $componentReturned")
            componentReturned
        }
    }

    //ComponentAmmo from database
    //THIS ONE
    private suspend fun getComponentAmmoListFromDatabase(): List<Ammo> {
        return withContext(Dispatchers.IO) {
            var componentAmmosReturned = mutableListOf<Ammo>()
            if(chosenComponent.value != null){
                componentAmmosReturned.addAll(ammoDatabase.getDefaultAmmoForThisComponent(
                    chosenComponent.value!!.componentAutoId))
                componentAmmosReturned.addAll(ammoDatabase.getComponentAmmosForThisComponentExceptDefault(
                    chosenComponent.value!!.componentAutoId)
                )

            }
            componentAmmosReturned
            // Log.i("Called1" ,"from db comp ammos: $componentAmmosReturned")

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
    private suspend fun getWeaponFromDatabaseUsingType(type: String): Component {
        return withContext(Dispatchers.IO) {
            val weaponReturned = compoDatabase.getUsingType(type)
            weaponReturned
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
    private suspend fun getWeaponFromDatabaseUsingDesc(desc: String): Component {
        return withContext(Dispatchers.IO) {
            val weaponReturned = compoDatabase.getUsingDesc(desc)
            weaponReturned
        }
    }

    fun doneNavigationToAddAnother() {
        _navigateToAddAnotherWeaponForCalculation.value = null
    }


    /**
     * Calculation
     */

    //TODO: need to add verification of all fields

    init {

        assaultIntensityString.value = "Training"
        if (calculationKey < 0) {
            initializeCalculation()
            visibilityDaysAndIntensity.value = true
            visibilityDaysAndIntensityOutput.value = false
        } else {
            visibilityDaysAndIntensity.value = false
            visibilityDaysAndIntensityOutput.value = true
            setTypeAndDays()

        }

        initializeSingle()
    }


    fun onCalculate() {
        uiScope.launch {
            _navigateToOutput.value = calculation.value
          //  Log.i("crash5", "${calculation.value}")
        }
    }

    //
    fun doneNavigationToOutput() {
        _navigateToOutput.value = null
    }


    private fun initializeSingle() {
        uiScope.launch {
            val newSingle = PerWeaponCalculation()
            insertSingleWeapon(newSingle)
            singleWeapon.value = getSingleWeaponFromDatabase()
        }
    }


    private suspend fun getSingleWeaponFromDatabase(): PerWeaponCalculation? {
        return withContext(Dispatchers.IO) {
            val weapon = perWeaponCalculationDatabase.getNewCalculation()
            weapon
        }
    }

    private fun initializeCalculation() {
        uiScope.launch {
            val newCalculation = Calculation()
            newCalculation.numberOfDays = getDays()
            newCalculation.assaultIntensity = assaultIntensityString.value!!
            insertCalculation(newCalculation)
            calculation.value = newCalculation
            calculationKey = getCalculationFromDatabase()!!.calculationId
            newCalculation.calculationId = calculationKey

            updateCalculationsDatabase(newCalculation)
            //     Log.i("calc", "SET init $newCalculation")
            //    Log.i("calc", "SET init ${assaultIntensityString.value}")

        }
    }

    private fun setTypeAndDays() {
        uiScope.launch {
            val calc = getCalculationFromDatabaseUsingID(calculationKey)
            assaultIntensityString.value = calc!!.assaultIntensity
            calculation.value = calc
            //combatIntToString(assaultIntensity)
            daysChosen.value = calc.numberOfDays.toString()

            //   Log.i("calc", "SET DAY TYPE $calc")
            //   Log.i("calc", "SET DAY TYPE ${assaultIntensityString.value.toString()}")
        }
    }


    private suspend fun getCalculationFromDatabaseUsingID(id: Long): Calculation? {
        return withContext(Dispatchers.IO) {
            val calc = calculationDatabase.get(id)
            calc
        }
    }

    private suspend fun getCalculationFromDatabase(): Calculation? {
        return withContext(Dispatchers.IO) {
            val calc = calculationDatabase.getNewCalculation()
            calc
        }
    }

    fun onSubmitDayAndIntensity() {
        uiScope.launch {
            val thisCalc = calculation.value ?: return@launch
            thisCalc.assaultIntensity = assaultIntensityString.value!!

            thisCalc.numberOfDays = getDays()
            updateCalculationsDatabase(thisCalc)
            visibilityDaysAndIntensityOutput.value = true
            visibilityDaysAndIntensity.value = false
            daysChosen.value = thisCalc.numberOfDays.toString()
            // Log.i("calc", "submit ${calculation.value}")
        }
    }

    fun onAddWeapon() {
        uiScope.launch {
            val thisPerCalculation = singleWeapon.value ?: return@launch
            thisPerCalculation.group_calculationID = calculationKey
            thisPerCalculation.numberOfWeapons = getNumberWeapons()
            thisPerCalculation.weaponIDCalculation = _chosenWeapon.value!!.weaponId //from componentID 12/17
            thisPerCalculation.weaponAmmoIdCalculation = chosenAmmo.value!!.ammoAutoId
            if (chosenComponent.value?.componentTypeId != "" && chosenComponentAmmo.value != null) {
                thisPerCalculation.componentAmmoIdCalculation =
                    chosenComponentAmmo.value!!.ammoAutoId
                Log.i("err8", "chosenAmmo: ${chosenComponentAmmo.value}")
            } else {
               // Log.i("crash6", "A =0")
                thisPerCalculation.componentAmmoIdCalculation = 0
            }
            if (noComponentAmmo) {
               // Log.i("crash6", "B =0")
                thisPerCalculation.componentAmmoIdCalculation = 0
            }
            updateSingle(thisPerCalculation)
            noComponentAmmo = false
           Log.i("box11", "this weapon: $thisPerCalculation")
            _navigateToAddAnotherWeaponForCalculation.value = thisPerCalculation
        }
    }

    /**
     * Suspend and Utility Functions
     */

    private suspend fun updateSingle(thisweapon: PerWeaponCalculation) {
        withContext(Dispatchers.IO) {
            perWeaponCalculationDatabase.update(thisweapon)
        }
    }

    private suspend fun insertSingleWeapon(calc: PerWeaponCalculation) {
        withContext(Dispatchers.IO) {
            perWeaponCalculationDatabase.insert(calc)
        }

    }

    private suspend fun insertCalculation(newCalculation: Calculation) {
        withContext(Dispatchers.IO) {
            calculationDatabase.insert(newCalculation)
        }
    }

    private suspend fun updateCalculationsDatabase(newCalculation: Calculation) {
        withContext(Dispatchers.IO) {
            calculationDatabase.update(newCalculation)
        }
    }

//    private suspend fun updateSingleWeaponDatabase(calc: PerWeaponCalculation) {
//        withContext(Dispatchers.IO) {
//            perWeaponCalculationDatabase.update(calc)
//        }
//    }

    private fun getDays(): Int {
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

