package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.ComponentAmmo

@Dao
interface CalculationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: ComponentAmmo)

    @Update
    fun update(vararg  model: ComponentAmmo)

    @Delete
    fun delete(vararg  model: ComponentAmmo)

    @Query("SELECT * FROM component_ammo_table WHERE componentAmmoId = :key" )
    fun get(key: Long?): ComponentAmmo

    @Query("SELECT * FROM component_ammo_table")
    fun getAll(): LiveData<Array<ComponentAmmo>>

    @Query("SELECT count(*) FROM component_ammo_table")
    fun countAll() : Int

    @Query("SELECT * FROM component_ammo_table ORDER BY componentAmmoId DESC LIMIT 1" )
    fun getNewComponentAmmo() : ComponentAmmo?

    @Query("SELECT count(*) FROM component_ammo_table WHERE weapon_id_for_component_ammo = :key")
    fun countAllComponentAmmos(key: Long?) : Int

    @Query("SELECT * FROM component_ammo_table WHERE weapon_id_for_component_ammo =:key ")
    fun getComponentAmmoForThisWeapon(key: Long?) : ComponentAmmo
}