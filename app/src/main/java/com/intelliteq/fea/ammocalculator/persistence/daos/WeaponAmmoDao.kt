package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import com.intelliteq.fea.ammocalculator.persistence.models.WeaponAmmo

@Dao
interface WeaponAmmoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: WeaponAmmo)

    @Update
    fun update(vararg  model: WeaponAmmo)

    @Delete
    fun delete(vararg  model: WeaponAmmo)

    @Query("SELECT * FROM weapon_ammo_table WHERE ammoId = :key" )
    fun get(key: Long?): WeaponAmmo

    @Query("SELECT * FROM weapon_ammo_table")
    fun getAll(): LiveData<Array<WeaponAmmo>>

    @Query("SELECT count(*) FROM weapon_ammo_table")
    fun countAll() : Int

    @Query("SELECT * FROM weapon_ammo_table ORDER BY ammoId DESC LIMIT 1" )
    fun getNewWeapon() : WeaponAmmo?
}