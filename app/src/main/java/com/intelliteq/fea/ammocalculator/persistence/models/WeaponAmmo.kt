package com.intelliteq.fea.ammocalculator.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weapon_ammo_table")
data class WeaponAmmo (
    @PrimaryKey(autoGenerate = true)
    var ammoId: Long = 0L,

    @ColumnInfo(name = "weapon_for_ammo")
    var weaponId: Long = 0L,

    @ColumnInfo(name = "dodic_for_ammo")
    var DODIC: String = "",

    @ColumnInfo(name = "ammo_description")
    var ammoDescription: String = "",

    @ColumnInfo(name = "training_rating_ammo")
    var trainingRate: Int = 0,

    @ColumnInfo(name = "security_rating_ammo")
    var securityRate: Int = 0,

    @ColumnInfo(name = "sustain_rating_ammo")
    var sustainRate: Int = 0,

    @ColumnInfo(name = "light_assault_rating_ammo")
    var lightAssaultRate: Int = 0,

    @ColumnInfo(name = "medium_assault__rating_ammo")
    var mediumAssaultRate: Int = 0,

    @ColumnInfo(name = "heavy_assault_rating_ammo")
    var heavyAssaultRate: Int = 0,

    @ColumnInfo(name="ammo_type")
    var ammoType: String = ""

)

