package com.intelliteq.fea.ammocalculator.persistence.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.intelliteq.fea.ammocalculator.persistence.models.Component


@Dao
interface ComponentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(component: Component)

    @Update
    fun update(component: Component)

    @Delete
    fun delete(component: Component)

    @Query("SELECT * FROM component_table WHERE componentAutoId = :key" )
    fun get(key: Long?): Component

    @Query("SELECT * FROM component_table WHERE fea_id_component = :key" )
    fun getUsingFEA(key: Long?): Component

    @Query("SELECT * FROM component_table WHERE component_type_id = :key")
    fun getUsingType(key: String) : Component

    @Query("SELECT * FROM component_table WHERE component_description = :key")
    fun getUsingDesc(key: String) : Component

    @Query("SELECT * FROM component_table")
    fun getAll(): LiveData<Array<Component>>

    @Query("SELECT count(*) FROM component_table")
    fun countAll() : Int

    @Query("SELECT * FROM component_table ORDER BY componentAutoId DESC LIMIT 1" )
    fun getNewComponent() : Component?

    @Query("SELECT count(*) FROM component_table WHERE weapon_id_for_component = :key")
    fun countAllComponents(key: Long?) : Int

    @Query("SELECT * FROM component_table WHERE weapon_id_for_component = :key AND bool_weapon = 0")
    fun getAllComponentsForThisWeapon(key: Long?) : List<Component>

    @Query("SELECT * FROM component_table WHERE weapon_id_for_component = :key AND bool_weapon = 0")
    fun getAllComponents(key: Long?) : LiveData<List<Component>>

    @Query("SELECT * FROM component_table WHERE bool_weapon = 1 ")
    fun getAllWeapons() : LiveData<List<Component>>

    @Query("SELECT * FROM component_table WHERE weapon_id_for_component = :key")
    fun getCompID(key: Long) : Long

    @Query("SELECT * FROM component_table WHERE weapon_id_for_component = :key AND bool_weapon=1")
    fun getWeapon(key: Long) : Component?
}