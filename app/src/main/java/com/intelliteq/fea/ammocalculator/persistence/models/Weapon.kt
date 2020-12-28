package com.intelliteq.fea.ammocalculator.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weapon_table")
data class Weapon(
    @PrimaryKey(autoGenerate = true)
    var weaponAutoId: Long = 0L,

    @ColumnInfo(name = "component_id")
    var componentId: Long = 0L

)
