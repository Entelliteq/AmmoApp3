package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
//import com.intelliteq.fea.ammocalculator.persistence.models.WeaponAmmo
//
//@Dao
//interface WeaponAmmoDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(vararg model: WeaponAmmo)
//
//    @Update
//    fun update(vararg  model: WeaponAmmo)
//
//    @Delete
//    fun delete(vararg  model: WeaponAmmo)
//
//    @Query("SELECT * FROM weapon_ammo_table WHERE ammoId = :key" )
//    fun get(key: Long?): WeaponAmmo
//
//    @Query("SELECT * FROM weapon_ammo_table")
//    fun getAll(): LiveData<List<WeaponAmmo>>
//
//    @Query("SELECT * FROM weapon_ammo_table WHERE weapon_for_ammo = :key")
//    fun getAllAmmosWithThisWeapon(key: Long) : LiveData<List<WeaponAmmo>>
//
//    @Query("SELECT count(*) FROM weapon_ammo_table")
//    fun countAll() : Int
//
//    @Query("SELECT * FROM weapon_ammo_table ORDER BY ammoId DESC LIMIT 1" )
//    fun getNewAmmo() : WeaponAmmo?
//
//    //get count of all ammos with this weapon id
//    @Query("SELECT count(*) FROM weapon_ammo_table WHERE weapon_for_ammo = :key")
//    fun countAllAmmos(key: Long?) : Int
//
//    @Query("SELECT * FROM weapon_ammo_table WHERE weapon_for_ammo = :key")
//    fun getAllAmmosForThisWeapon(key: Long?) : List<WeaponAmmo>
//
//    @Query("SELECT * FROM weapon_ammo_table WHERE  ammo_type = :typeKey")
//    fun getAmmoType(typeKey: String) : WeaponAmmo?
//}