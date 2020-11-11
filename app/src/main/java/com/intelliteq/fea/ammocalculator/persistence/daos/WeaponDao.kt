package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon

@Dao
interface WeaponDao {

    @Insert
    fun insert(weapon: Weapon)

    @Update
    fun update(weapon: Weapon)

    @Delete
    fun delete(weapon: Weapon)

    @Query("SELECT * FROM weapon_table WHERE weaponAutoId = :key" )
    fun get(key: Long): Weapon?

    @Query("SELECT * FROM weapon_table")
    fun getAllWeapons(): LiveData<List<Weapon>>

    @Query("SELECT count(*) FROM weapon_table")
    fun countAll() : Int

    @Query("SELECT * FROM weapon_table ORDER BY weaponAutoId DESC LIMIT 1" )
    fun getNewWeapon() : Weapon?


    @Query("SELECT w.*, c.* FROM weapon_table w, component_table c WHERE  w.weaponAutoId=c.weapon_id_for_component AND c.component_type_id = :typeKey ")
    fun getWeaponByType(typeKey: String) : Weapon?

    @Query("SELECT * FROM weapon_table WHERE weaponAutoId = :key" )
    fun getWeapon(key: Long): LiveData<Weapon>

}