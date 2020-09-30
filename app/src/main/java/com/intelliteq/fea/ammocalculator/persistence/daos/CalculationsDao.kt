package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.Calculations
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon

@Dao
interface CalculationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: Calculations)

    @Update
    fun update(vararg  model: Calculations)

    @Delete
    fun delete(vararg  model: Calculations)

    @Query("SELECT * FROM calculations_table WHERE calculationId = :key" )
    fun get(key: Long?): Calculations

    @Query("SELECT count(*) FROM calculations_table")
    fun countAll() : Int

    @Query("SELECT * FROM calculations_table ORDER BY calculationId DESC LIMIT 1" )
    fun getNewCalculation() : Calculations


    @Query("SELECT * FROM calculations_table WHERE name_of_calculation =:key")
    fun getGroupCalculationByName(key: String) : Calculations


    @Query("SELECT w.* FROM  calculations_table c, single_calculation_table s, weapon_table w WHERE c.calculationId = :key AND s.id_group_calculation = c.calculationId AND w.weaponAutoId = s.weapon_id_for_calculation")
    fun getSelectedWeapons(key: Long) : LiveData<List<Weapon>>

    @Query("SELECT * FROM calculations_table WHERE calculationId = :key LIMIT 1")
    fun getOneCalc(key: Long) : LiveData<Calculations>

}