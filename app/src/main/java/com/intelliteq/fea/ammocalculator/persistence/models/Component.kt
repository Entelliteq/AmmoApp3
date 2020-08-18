package com.intelliteq.fea.ammocalculator.persistence.models

import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "component_table")
class Component (

    @PrimaryKey(autoGenerate = true)
    var componentId: Long =0L,

    @ColumnInfo(name = "weapon_for_component")
    var weaponId: Long = 0,

    @ColumnInfo(name= "fea_id_component")
    var FEA_id: Int = 0,

    @ColumnInfo(name = "component_type_id")
    var componentTypeID: String = "",

    @ColumnInfo(name = "component_description")
    var componentDescription: String = ""
)