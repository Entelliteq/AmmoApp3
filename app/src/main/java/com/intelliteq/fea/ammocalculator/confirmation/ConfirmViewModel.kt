package com.intelliteq.fea.ammocalculator.confirmation


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.intelliteq.fea.ammocalculator.persistence.daos.ComponentAmmoDao
import com.intelliteq.fea.ammocalculator.persistence.daos.WeaponDao
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.ComponentAmmo
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import com.intelliteq.fea.ammocalculator.persistence.models.WeaponAmmo
import kotlinx.android.synthetic.main.fragment_confirmation.*
import kotlinx.coroutines.*

class ConfirmViewModel(
    val database: AmmoRoomDatabase,
    application: Application) : AndroidViewModel(application) {
    private val weaponDAO = database.weaponDao
    private val weaponAmmoDAO = database.weaponAmmoDao
    private val componentDAO = database.componentDao
    private val componentAmmoDAO = database.componentAmmoDao


    /*********************************************************
     * getting component ammos from data
     *******************************************************/
    suspend fun getWeapon(weaponId: Long):Weapon?{
        return getWeaponFromData(weaponId)
    }

    /**
     * Suspend function to get Weapon from database
     * @param weapon: to be updated
     */
    private suspend fun getWeaponFromData(weaponId: Long): Weapon? {
        return withContext(Dispatchers.IO) {
            var weapon: Weapon? = weaponDAO.get(weaponId)
            weapon
        }
    }


    /*********************************************************
     * getting weapon ammos from data
     *******************************************************/
    suspend fun getWeaponAmmo(weaponId: Long): List<WeaponAmmo> {
        return getWeaponAmmoFromData(weaponId)
    }

    /**
     * Suspend function to get Weapon from database
     * @param weapon: to be updated
     */
    private suspend fun getWeaponAmmoFromData(weaponId: Long): List<WeaponAmmo> {
        return withContext(Dispatchers.IO) {
            var weaponAmmo: List<WeaponAmmo> = weaponAmmoDAO.getAllAmmosForThisWeapon(weaponId)
            weaponAmmo
        }
    }


    /*********************************************************
     * getting component from data
     *******************************************************/
    suspend fun getComponent(weaponId: Long): List<Component> {
        return getComponentFromData(weaponId)
    }

    /**
     * Suspend function to get Weapon from database
     * @param weapon: to be updated
     */
    private suspend fun getComponentFromData(weaponId: Long): List<Component> {
        return withContext(Dispatchers.IO) {
            var component: List<Component> = componentDAO.getAllComponentsForThisWeapon(weaponId)
            component
        }
    }

    /*********************************************************
     * getting component ammos from data
     *******************************************************/
    suspend fun getComponentAmmo(weaponId: Long): List<ComponentAmmo> {
        return getComponentAmmoFromData(weaponId)
    }

    /**
     * Suspend function to get Weapon from database
     * @param weapon: to be updated
     */
    private suspend fun getComponentAmmoFromData(weaponId: Long): List<ComponentAmmo> {
        return withContext(Dispatchers.IO) {
            var componentAmmo: List<ComponentAmmo> = componentAmmoDAO.getComponentAmmosForThisComponent(weaponId)
            componentAmmo
        }
    }
}