package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.Calculations

@Dao
interface AmmoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: Ammo)

    @Update
    fun update(vararg  model: Ammo)

    @Delete
    fun delete(vararg  model: Ammo)

    @Query("SELECT * FROM ammo_table WHERE  ammo_type_id = :key" )
    fun get(key: String?): Ammo

    @Query("SELECT count(*) FROM ammo_table")
    fun countAll() : Int


}