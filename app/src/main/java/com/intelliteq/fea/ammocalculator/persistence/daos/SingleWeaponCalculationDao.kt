package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.ComponentAmmo
import com.intelliteq.fea.ammocalculator.persistence.models.SingleWeaponCalculation
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import com.intelliteq.fea.ammocalculator.persistence.models.WeaponAmmo

@Dao
interface SingleWeaponCalculationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: SingleWeaponCalculation)

    @Update
    fun update(vararg  model: SingleWeaponCalculation)

    @Delete
    fun delete(vararg  model: SingleWeaponCalculation)

    @Query("SELECT * FROM single_calculation_table WHERE calculationId = :key" )
    fun get(key: Long?): SingleWeaponCalculation

    @Query("SELECT count(*) FROM single_calculation_table")
    fun countAll() : Int

    @Query("SELECT * FROM single_calculation_table ORDER BY calculationId DESC LIMIT 1" )
    fun getNewCalculation() : SingleWeaponCalculation

    @Query("SELECT count(*) FROM single_calculation_table WHERE calculationId = :key")
    fun countAllCalculations(key: Long?) : Int

    @Query("SELECT * FROM single_calculation_table WHERE weapon_id_for_calculation = :key")
    fun getWeaponForCalculation(key: Long?) : Long

    @Query("SELECT * FROM single_calculation_table WHERE ammo_id_for_calculation = :key")
    fun getWeaponAmmoForCalculation(key: Long?) : List<SingleWeaponCalculation>

    @Query("SELECT * FROM single_calculation_table WHERE component_ammo_id_for_calculation = :key")
    fun getComponentAmmoForCalculation(key: Long?) : List<SingleWeaponCalculation>


}