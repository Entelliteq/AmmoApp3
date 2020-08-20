package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import com.intelliteq.fea.ammocalculator.persistence.models.WeaponAmmo


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

    @Query("SELECT * FROM component_table ORDER BY componentId DESC LIMIT 1" )
    fun getNewComponent() : Component?

    @Query("SELECT count(*) FROM component_table WHERE weapon_for_component = :key")
    fun countAllComponents(key: Long?) : Int

    @Query("SELECT * FROM component_table WHERE weapon_for_component = :key")
    fun getAllComponentsForThisWeapon(key: Long?) : Component
}