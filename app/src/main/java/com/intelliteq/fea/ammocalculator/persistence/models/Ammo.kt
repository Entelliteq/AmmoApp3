package com.intelliteq.fea.ammocalculator.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName= "ammo_table")
open class Ammo (
    @ColumnInfo(name = "ammo_type_id")
    var ammoTypeID: String? = "",

    @ColumnInfo(name = "ammo_description")
    var ammoDescription: String? = "",

    @ColumnInfo(name="ammo_dodic")
    var ammoDODIC: String? = "",

    @ColumnInfo(name= "ammo_weapon_id")
    var ammoWeaponId: Long = 0L
)