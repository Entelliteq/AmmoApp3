package com.intelliteq.fea.ammocalculator.savedWeapons

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import kotlinx.coroutines.Job

class SavedWeaponsViewModel (
    componentDatabase: ComponentDao

) : ViewModel() {

    //Job and CoroutineScope
    private val viewModelJob = Job()

    val weapons = componentDatabase.getAllWeapons()

    private val _navigateToModifyWeapon = MutableLiveData<Component>()
    val naviagteToModifyWeapon
        get() = _navigateToModifyWeapon

    fun onWeaponClicked(id: Component) {
        _navigateToModifyWeapon.value = id
    }

    fun onModifyWeaponNavigated() {
        _navigateToModifyWeapon.value = null
    }


    /**
     * Cancelling all jobs
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}