package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon


@Dao
interface ComponentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: Component)

    @Update
    fun update(vararg  model: Component)

    @Delete
    fun delete(vararg  model: Component)

    @Query("SELECT * FROM component_table WHERE componentId = :key" )
    fun get(key: Long?): Component

    @Query("SELECT * FROM component_table")
    fun getAll(): LiveData<Array<Component>>

    @Query("SELECT count(*) FROM component_table")
    fun countAll() : Int
}