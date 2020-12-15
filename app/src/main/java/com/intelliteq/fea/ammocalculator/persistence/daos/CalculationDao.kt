package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
import com.intelliteq.fea.ammocalculator.persistence.models.Component

@Dao
interface CalculationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: Calculation)

    @Update
    fun update(vararg  model: Calculation)

    @Delete
    fun delete(vararg  model: Calculation)

    @Query("SELECT * FROM calculation_table WHERE calculationId = :key" )
    fun get(key: Long?): Calculation

    @Query("SELECT * FROM calculation_table WHERE calculationId = :key" )
    fun getThisCalculation(key: Long?): LiveData<Calculation>

    @Query("SELECT * FROM calculation_table WHERE calculationId = :key" )
    fun getThisCalculationWithName(key: String): LiveData<Calculation>

    @Query("SELECT count(*) FROM calculation_table")
    fun countAll() : Int

    @Query("SELECT * FROM calculation_table ORDER BY calculationId DESC LIMIT 1" )
    fun getNewCalculation() : Calculation

    @Query("SELECT * FROM calculation_table WHERE name_of_calculation =:key")
    fun getGroupCalculationByName(key: String) : Calculation

//    @Query("SELECT w.* FROM  calculation_table c, single_calculation_table s, weapon_table w WHERE c.calculationId = :key AND s.id_group_calculation = c.calculationId AND w.weaponAutoId = s.weapon_id_for_calculation")
//    fun getSelectedWeapons(key: Long) : LiveData<List<Weapon>>

    @Query("SELECT w.* FROM  calculation_table c, single_calculation_table s, component_table w WHERE c.calculationId = :key AND s.id_group_calculation = c.calculationId AND  s.weapon_id_for_calculation = w.componentAutoId AND w.bool_weapon = 1" )
    fun getSelectedWeapons(key: Long) : LiveData<List<Component>>

    @Query("SELECT * FROM calculation_table WHERE calculationId = :key LIMIT 1")
    fun getOneCalc(key: Long) : LiveData<Calculation>

    //redo query
//    @Query("SELECT a.* FROM calculation_table c, single_calculation_table s, ammo_table a WHERE c.calculationId = :key AND s.id_group_calculation = c.calculationId AND s.weapon_id_for_calculation = a.ammoAutoId AND a.bool_weapon_ammo = 1 union SELECT a.* FROM calculation_table c, single_calculation_table s, ammo_table a WHERE c.calculationId = :key AND s.id_group_calculation = c.calculationId AND s.component_ammo_id_for_calculation = a.component_id_for_ammo AND a.bool_weapon_ammo = 0")
//    fun getSelectedAmmos(key: Long) : LiveData<List<Ammo>>

    //correct
//    @Query("SELECT a.* FROM calculation_table c, single_calculation_table s, ammo_table a WHERE c.calculationId = :key AND s.id_group_calculation = c.calculationId AND s.ammo_id_for_calculation = a.ammoAutoId AND a.bool_weapon_ammo = 1 union SELECT a.* FROM calculation_table c, single_calculation_table s, ammo_table a WHERE c.calculationId = :key AND s.id_group_calculation = c.calculationId AND s.component_ammo_id_for_calculation = a.ammoAutoId AND a.bool_weapon_ammo = 0")
//    fun getSelectedAmmos(key: Long) : LiveData<List<Ammo>>

    @Query("SELECT a.* FROM calculation_table c, single_calculation_table s, ammo_table a WHERE c.calculationId = :key AND s.id_group_calculation = c.calculationId AND s.ammo_id_for_calculation = a.ammoAutoId union all SELECT a.* FROM calculation_table c, single_calculation_table s, ammo_table a WHERE c.calculationId = :key AND s.id_group_calculation = c.calculationId AND s.component_ammo_id_for_calculation = a.ammoAutoId ")
    fun getSelectedAmmos(key: Long) : LiveData<List<Ammo>>

    @Query("SELECT a.* FROM calculation_table c, single_calculation_table s, ammo_table a WHERE c.calculationId = :key AND s.id_group_calculation = c.calculationId AND s.ammo_id_for_calculation = a.ammoAutoId union all SELECT a.* FROM calculation_table c, single_calculation_table s, ammo_table a WHERE c.calculationId = :key AND s.id_group_calculation = c.calculationId AND s.component_ammo_id_for_calculation = a.ammoAutoId ")
    fun getSelectedAmmosList(key: Long) : List<Ammo>


    @Query("select * from calculation_table where name_of_calculation > 0  ")
    fun getAllSavedCalculations() : LiveData<List<Calculation>>

}