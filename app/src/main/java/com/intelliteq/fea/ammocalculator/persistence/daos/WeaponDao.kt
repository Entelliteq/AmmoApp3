package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon

@Dao
interface WeaponDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //added testing push
    fun insert(weapon: Weapon)

    @Update
    fun update(weapon: Weapon)

    @Delete
    fun delete(weapon: Weapon)

    @Query("SELECT * FROM weapon_table WHERE weaponAutoId = :key" )
    fun get(key: Long): Weapon?

    @Query("SELECT * FROM weapon_table")
    fun getAll(): LiveData<Array<Weapon>>

    @Query("SELECT count(*) FROM weapon_table")
    fun countAll() : Int

    @Query("SELECT * FROM weapon_table ORDER BY weaponAutoId DESC LIMIT 1" )
    fun getNewWeapon() : Weapon?


}