package com.intelliteq.fea.ammocalculator.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_table")
class Calculation (

    @PrimaryKey(autoGenerate = true)
    var calculationId: Long =0L,

    @ColumnInfo(name = "weapon_for_calculation")
    var weaponId: Long = 0,

    @ColumnInfo(name= "fea_id_calculation")
    var FEA_id: Int = 0,

    @ColumnInfo(name = "component_type_id")
    var componentTypeID: String = "",

    @ColumnInfo(name = "component_description")
    var componentDescription: String = ""
)