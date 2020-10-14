package com.intelliteq.fea.ammocalculator.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "ammo_table")
open class Ammo (

//    @PrimaryKey(autoGenerate = true)
//    var ammoIdAmmo: Long =0L,

    @ColumnInfo(name = "ammo_type_id")
    var ammoTypeID: String? = "",

    @ColumnInfo(name = "ammo_description")
    var ammoDescription: String? = "",

    @ColumnInfo(name="ammo_dodic")
    var ammoDODIC: String? = "",

    @ColumnInfo(name= "ammo_weapon_id")
    var ammoWeaponId: Long = 0L
)