package com.intelliteq.fea.ammocalculator.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_of_calculations_table")
class GroupCalculation (

    @PrimaryKey(autoGenerate = true)
    var calculationId: Long =0L,

    @ColumnInfo(name = "name_of_calculation")
    var calculationName: String = "",

    @ColumnInfo(name= "assault_type")
    var assaultType: Int = 0,

    @ColumnInfo(name = "num_days")
    var numberOfDays: Int = 0,

    @ColumnInfo(name = "calculation_list")
    var weaponCalculations: List<SingleWeaponCalculation> = listOf())




