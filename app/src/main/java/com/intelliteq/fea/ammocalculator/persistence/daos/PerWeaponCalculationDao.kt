package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.PerWeaponCalculation

@Dao
interface PerWeaponCalculationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: PerWeaponCalculation)

    @Update
    fun update(vararg  model: PerWeaponCalculation)

    @Delete
    fun delete(vararg  model: PerWeaponCalculation)

    @Query("SELECT * FROM single_calculation_table WHERE single_calculationId = :key" )
    fun get(key: Long?): PerWeaponCalculation

    @Query("SELECT count(*) FROM single_calculation_table")
    fun countAll() : Int

    @Query("SELECT * FROM single_calculation_table ORDER BY single_calculationId DESC LIMIT 1" )
    fun getNewCalculation() : PerWeaponCalculation

    @Query("SELECT count(*) FROM single_calculation_table WHERE single_calculationId = :key")
    fun countAllCalculations(key: Long?) : Int

    @Query("SELECT * FROM single_calculation_table WHERE weapon_id_for_calculation = :key")
    fun getWeaponForCalculation(key: Long?) : Long

    @Query("SELECT * FROM single_calculation_table WHERE ammo_id_for_calculation = :key")
    fun getWeaponAmmoForCalculation(key: Long?) : List<PerWeaponCalculation>

    @Query("SELECT * FROM single_calculation_table WHERE component_ammo_id_for_calculation = :key")
    fun getComponentAmmoForCalculation(key: Long?) : List<PerWeaponCalculation>

    @Query("SELECT * FROM single_calculation_table WHERE id_group_calculation = :key ")
    fun getUsingCalculationID(key: Long) : LiveData<List<PerWeaponCalculation>>

    @Query("SELECT * FROM single_calculation_table WHERE id_group_calculation = :key ")
    fun getUsingCalculationIDList(key: Long) : List<PerWeaponCalculation>

    @Query("SELECT * FROM single_calculation_table WHERE id_group_calculation = :key ")
    fun getUsingCalculationIDLive(key: Long) : LiveData<PerWeaponCalculation>

    //getUsingWeaponAndCalcID(calculationKey, item.componentTypeId)
    @Query("select * from single_calculation_table where id_group_calculation =:key and weapon_id_for_calculation = :id")
    fun getUsingWeaponAndCalcID(key: Long, id: Long) : PerWeaponCalculation
  //  fun getUsingWeaponAndCalcID(key: Long, id: Long) : List<PerWeaponCalculation>



}