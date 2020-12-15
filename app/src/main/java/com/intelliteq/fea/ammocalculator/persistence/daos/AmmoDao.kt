package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo


@Dao
interface AmmoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: Ammo)

    @Update
    fun update(vararg  model: Ammo)

    @Delete
    fun delete(vararg  model: Ammo)

    @Query("SELECT * FROM ammo_table WHERE ammoAutoId = :key" )
    fun get(key: Long?): Ammo

    @Query("SELECT * FROM ammo_table")
    fun getAll(): LiveData<Array<Ammo>>

    @Query("SELECT count(*) FROM ammo_table")
    fun countAll() : Int

    @Query("SELECT * FROM ammo_table ORDER BY ammoAutoId DESC LIMIT 1" )
    fun getNewAmmo() : Ammo?


    @Query("SELECT  * FROM ammo_table  WHERE bool_weapon_ammo = 1 AND weapon_id_for_ammo = :key  ")//AND c.componentAutoId=a.component_id_for_ammo ")
    fun getAllAmmosForThisWeapon(key: Long?) : List<Ammo>

    @Query("SELECT  * FROM ammo_table  WHERE bool_weapon_ammo = 1 AND weapon_id_for_ammo = :key  AND default_ammo = 0")//AND c.componentAutoId=a.component_id_for_ammo ")
    fun getAllAmmosForThisWeaponExceptDefault(key: Long?) : List<Ammo>

    @Query("SELECT  * FROM ammo_table  WHERE bool_weapon_ammo = 1 AND weapon_id_for_ammo = :key  AND default_ammo = 1")//AND c.componentAutoId=a.component_id_for_ammo ")
    fun getDefaultAmmoForThisWeapon(key: Long?) : List<Ammo>

    @Query("SELECT * FROM ammo_table WHERE  ammo_dodic = :typeKey AND weapon_id_for_ammo = :weaponKey")
    fun getAmmoType(typeKey: String, weaponKey: Long) : Ammo?

    @Query("SELECT * FROM ammo_table WHERE ammo_dodic = :key")
    fun getUsingType(key: String) : Ammo

    @Query("SELECT * FROM ammo_table WHERE component_id_for_ammo = :key AND bool_weapon_ammo = 0")
    fun getComponentAmmosForThisComponent(key: Long?) : List<Ammo>

    @Query("SELECT * FROM ammo_table WHERE component_id_for_ammo = :key AND bool_weapon_ammo = 0 AND default_ammo = 0")
    fun getComponentAmmosForThisComponentExceptDefault(key: Long?) : List<Ammo>

    @Query("SELECT * FROM ammo_table WHERE component_id_for_ammo = :key AND bool_weapon_ammo = 0 AND default_ammo = 1")
    fun getDefaultAmmoForThisComponent(key: Long?) : List<Ammo>

    @Query("SELECT  * FROM ammo_table  WHERE bool_weapon_ammo = 1 AND weapon_id_for_ammo = :key  ")//AND c.componentAutoId=a.component_id_for_ammo ")
    fun getAllWeaponAmmos(key: Long) : LiveData<List<Ammo>>

    @Query("SELECT  * FROM ammo_table  WHERE bool_weapon_ammo = 1 AND weapon_id_for_ammo = :key  ")//AND c.componentAutoId=a.component_id_for_ammo ")
    fun getAllWeaponAmmoList(key: Long) : List<Ammo>

    @Query("SELECT  * FROM ammo_table  WHERE bool_weapon_ammo = 0 AND weapon_id_for_ammo = :key  ")//AND c.componentAutoId=a.component_id_for_ammo ")
    fun getAllComponentAmmos(key: Long) : LiveData<List<Ammo>>
}