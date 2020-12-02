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
    val ammoDatabase: AmmoDao,
    val compoDatabase: ComponentDao,
    val perWeaponCalculationDatabase: PerWeaponCalculationDao,
    val calculationDatabase: CalculationDao
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
    val numberOfDaysPicker: LiveData<Int>
        get() = _numberOfDaysPicker

    private val _numberOfWeaponsPicker = MutableLiveData<Int>()
    val numberOfWeaponsPicker: LiveData<Int>  //TODO: maybe no getter needed?
        get() = _numberOfWeaponsPicker

    //ammo
    private val _chosenAmmoList = MutableLiveData<List<Ammo>>()
    val chosenAmmoList: LiveData<List<Ammo>>
        get() = _chosenAmmoList

    private val _chosenAmmo = MutableLiveData<Ammo>()
    val chosenAmmo: LiveData<Ammo>
        get() = _chosenAmmo

    //weapon
    private val _chosenWeapon = MutableLiveData<Component>()
    val chosenWeapon: LiveData<Component>
        get() = _chosenWeapon

    //component
    private val _chosenComponentList = MutableLiveData<List<Component>>()
    val chosenComponentList: LiveData<List<Component>>
        get() = _chosenComponentList

    private val _chosenComponent = MutableLiveData<Component>()
    val chosenComponent: LiveData<Component>
        get() = _chosenComponent

    //TODO: delete?
    private val _chosenComponentAmmo = MutableLiveData<Ammo>()
    val chosenComponentAmmo: LiveData<Ammo>
        get() = _chosenComponentAmmo


    //TODO: delete?
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


    fun doneSpinnerComp() {
        _chosenComponent.value = Component()
     //   Log.i("error", "called done spinner comp")
    }

    fun doneSpinnerCompAmmo() {
        _chosenComponentAmmo.value = Ammo()
    }

    /**
     * Assault Intensity
     * Which intensity is picked by user, then need to get values
     * from database based on the chosen assault intensity
     */
    fun assaultIntensityStringToIntValues(combat: String) {
        if (combat == "none") {
            assaultIntensityString.value = "Training"
        }
        else {
            assaultIntensityString.value = combat
        }

//        when (combat) {
//            "Training" -> assaultIntensity = 1
//            "Security" -> assaultIntensity = 2
//            "Sustain" -> assaultIntensity = 3
//            "Light Assault" -> assaultIntensity = 4
//            "Medium Assault" -> assaultIntensity = 5
//            else -> assaultIntensity = 6
//        }
       // Log.i("CALC", "TO INT ${assaultIntensityString.value}")
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
            Log.i("CALC", "///weaponId:${chosenWeapon.value!!.componentDescription}")
            Log.i("CALC", "///ammoList:${chosenAmmoList.value}")
            Log.i("CALC", "///complist:${chosenComponentList.value}")
            if(chosenComponentList.value!!.isEmpty()) {
                useComponent("")
            }
        }
    }

    //Component List from database
    private suspend fun getChosenComponentFromDatabase(): List<Component> {
        return withContext(Dispatchers.IO) {
            var componentsReturned =
                compoDatabase.getAllComponentsForThisWeapon(chosenWeapon.value!!.weaponId)
            componentsReturned
        }
    }

    //Ammo List from database
    private suspend fun getChosenAmmoFromDatabaseUsingWeapon(): List<Ammo> {
        return withContext(Dispatchers.IO) {
            var ammosReturned =
                ammoDatabase.getAllAmmosForThisWeapon(chosenWeapon.value!!.weaponId)
           // Log.i("CALC", "///ammoListSuspend:${ammosReturned}")
          //  Log.i("CALC", "///chosenWeponSuspend:${chosenWeapon.value}")
            ammosReturned
        }
    }

    //Weapon from database
    private suspend fun getWeaponFromDatabaseUsingFEA(fea: Int): Component? {
        return withContext(Dispatchers.IO) {
            var weaponReturned = compoDatabase.getUsingFEA(fea.toLong())
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

    private suspend fun getChosenAmmoFromDatabase(ammoId: String): Ammo? {
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
          //  Log.i("error", "COMP_AMMO////: ${chosenComponentAmmo.value}")
          //  Log.i("error", "string////: ${compID}")
        }
    }

    private suspend fun getCompAmmoFromDatabase(compID: String): Ammo? {
        return withContext(Dispatchers.IO) {
            var compAmmoReturned = ammoDatabase.getUsingType(compID)
            compAmmoReturned
        }
    }


    fun useComponent(compID: String) {
        getChosenComponent(compID)
        Log.i("Called", "here $compID")
        if(compID =="") {
            Log.i("Called" , "no string")
        }

    }


    private fun getChosenComponent(compID: String) {
        uiScope.launch {
            Log.i("type5 id", "__$compID")
          //  if(!compID.isNullOrEmpty()) {
                _chosenComponent.value = getComponentFromDatabase(compID)
                Log.i("type5", "Comp COMP: ${chosenComponent.value}")
                _chosenComponentAmmoList.value = getComponentAmmoListFromDatabase()
                if (_chosenComponentAmmoList.value.isNullOrEmpty()) {
                    noComponentAmmo = true
                }
                Log.i("Called1", "Comp AMMO: ${chosenComponentAmmoList.value}")
           // }
        }
    }

    private suspend fun getComponentFromDatabase(id: String): Component {
        return withContext(Dispatchers.IO) {
            var componentReturned = compoDatabase.getUsingType(id)
            //Log.i("Called" ,"from db: $componentReturned")
            componentReturned
        }
    }

    //ComponentAmmo from database
    private suspend fun getComponentAmmoListFromDatabase(): List<Ammo> {
        return withContext(Dispatchers.IO) {
            var componentAmmosReturned = ammoDatabase.getComponentAmmosForThisComponent(
                chosenComponent.value!!.componentAutoId )
            Log.i("Called1" ,"from db comp ammos: $componentAmmosReturned")
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


         //   Log.i("CALC", "///weaponId:${chosenWeapon.value!!.weaponId}")
          //  Log.i("CALC", "///ammoList:${chosenAmmoList.value}")
          //  Log.i("CALC", "///complist:${chosenComponentList.value}")
        }
    }

    //return from database
    private suspend fun getWeaponFromDatabaseUsingType(type: String): Component {
        return withContext(Dispatchers.IO) {
            var weaponReturned = compoDatabase.getUsingType(type)
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
    private suspend fun getWeaponFromDatabaseUsingDesc(desc: String): Component {
        return withContext(Dispatchers.IO) {
            var weaponReturned = compoDatabase.getUsingDesc(desc)
            weaponReturned!!
        }
    }


    /**
     * Calculation
     */

    //TODO: need to add verification of all fields

//

    fun doneNavigationToAddAnother() {
        _navigateToAddAnotherWeaponForCalculation.value = null
    }

    fun onCalculate() {
        uiScope.launch {
            _navigateToOutput.value = calculation.value
           Log.i("calc1" , "${calculation.value}" )
        }
    }

    //
    fun doneNavigationToOutput() {
        _navigateToOutput.value = null
    }

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
      //  Log.i("calc INIT", "Assault//${assaultIntensityString.value}")
    }

    fun initializeSingle() {
        uiScope.launch {
            val newSingle = PerWeaponCalculation()
            insertSingleWeapon(newSingle)
            singleWeapon.value = getSingleWeaponFromDatabase()
        }
    }


    private suspend fun getSingleWeaponFromDatabase(): PerWeaponCalculation? {
        return withContext(Dispatchers.IO) {
            var weapon = perWeaponCalculationDatabase.getNewCalculation()
            weapon
        }
    }

    fun initializeCalculation() {
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

    fun setTypeAndDays() {
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

//    fun combatIntToString(combat: Int) : String {
//
//        when (combat) {
//            1 -> assaultIntensityString.value = "Training"
//            2 -> assaultIntensityString.value = "Security"
//            3 -> assaultIntensityString.value = "Sustain"
//            4 -> assaultIntensityString.value = "Light Assault"
//            5 -> assaultIntensityString.value = "Medium Assault"
//            else -> assaultIntensityString.value = "Heavy Assault"
//        }
//        return assaultIntensityString.value.toString()
//    }


    private suspend fun getCalculationFromDatabaseUsingID(id: Long): Calculation? {
        return withContext(Dispatchers.IO) {
            var calc = calculationDatabase.get(id)
            calc
        }
    }

    private suspend fun getCalculationFromDatabase(): Calculation? {
        return withContext(Dispatchers.IO) {
            var calc = calculationDatabase.getNewCalculation()
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
            val thisWeapon = singleWeapon.value ?: return@launch
            thisWeapon.group_calculationID = calculationKey
            thisWeapon.numberOfWeapons = getNumberWeapons()
            thisWeapon.weaponIDCalculation = _chosenWeapon.value!!.componentAutoId
            thisWeapon.weaponAmmoIdCalculation = chosenAmmo.value!!.ammoAutoId
          //  Log.i("error1", "onAddWeapon: ${chosenComponentAmmo.value}")
            if(chosenComponent.value?.componentTypeId != "" && chosenComponentAmmo.value != null) {
                Log.i("crash3", "_${chosenComponent.value}_")
                thisWeapon.componentAmmoIdCalculation =
                    chosenComponentAmmo.value!!.ammoAutoId
                 Log.i("ifelse5", "here1")
            }
            else
            {
                thisWeapon.componentAmmoIdCalculation = 0
                Log.i("ifelse5", "here2")
            }
            if(noComponentAmmo) {
                thisWeapon.componentAmmoIdCalculation = 0
                Log.i("ifelse5", "here3")
            }
            updateSingle(thisWeapon)
            noComponentAmmo = false
            _navigateToAddAnotherWeaponForCalculation.value = thisWeapon
        }
    }

    //Suspend Functions

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

    private suspend fun updateSingleWeaponDatabase(calc: PerWeaponCalculation) {
        withContext(Dispatchers.IO) {
            perWeaponCalculationDatabase.update(calc)
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

