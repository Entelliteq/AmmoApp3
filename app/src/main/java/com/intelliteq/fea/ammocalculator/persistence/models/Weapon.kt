package com.intelliteq.fea.ammocalculator.persistence.models

import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weapon_table")
data class Weapon(
    @PrimaryKey(autoGenerate = true)
    var weaponAutoId: Long = 0L,

    @ColumnInfo(name= "weapon_id_unique")
    var weaponId: Int = 0,

    @ColumnInfo(name= "fea_id_weapon")
    var FEA_id: Int = 0,

    @ColumnInfo(name = "weapon_type_id")
    var weaponTypeID: String = "",

    @ColumnInfo(name = "weapon_description")
    var weaponDescription: String = ""
)
