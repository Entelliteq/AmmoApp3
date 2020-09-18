package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.GroupCalculation
import com.intelliteq.fea.ammocalculator.persistence.models.SingleWeaponCalculation

interface GroupCalculationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: GroupCalculation)

    @Update
    fun update(vararg  model: GroupCalculation)

    @Delete
    fun delete(vararg  model: GroupCalculation)

    @Query("SELECT * FROM group_of_calculations_table WHERE calculationId = :key" )
    fun get(key: Long?): GroupCalculation

    @Query("SELECT count(*) FROM group_of_calculations_table")
    fun countAll() : Int

    @Query("SELECT * FROM group_of_calculations_table ORDER BY calculationId DESC LIMIT 1" )
    fun getNewCalculation() : GroupCalculation

    @Query("SELECT count(*) FROM group_of_calculations_table WHERE calculationId = :key")
    fun countAllCalculations(key: Long?) : Int

    @Query("SELECT * FROM group_of_calculations_table WHERE name_of_calculation =:key")
    fun getGroupCalculationByName(key: String) : GroupCalculation

    @Query("SELECT * FROM group_of_calculations_table WHERE calculation_list = :key")
    fun getListOfSingleCalculations(key: Long) : List<SingleWeaponCalculation>
}