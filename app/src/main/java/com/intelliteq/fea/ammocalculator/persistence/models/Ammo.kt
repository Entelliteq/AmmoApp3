package com.intelliteq.fea.ammocalculator.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ammo_table")
data class Ammo (
    @PrimaryKey(autoGenerate = true)
    var ammoAutoId: Long =0L,

    @ColumnInfo(name = "weapon_id_for_ammo")
    var weaponId: Long = 0,

    @ColumnInfo(name = "component_id_for_ammo")
    var componentId: Long = 0,

    @ColumnInfo(name = "ammo_description")
    var ammoDescription: String? = "",

    @ColumnInfo(name="ammo_dodic")
    var ammoDODIC: String? = "",

    @ColumnInfo(name = "default_ammo")
    var defaultAmmo: Boolean = false,

    @ColumnInfo(name = "bool_weapon_ammo")
    var primaryAmmo: Boolean = false,

    @ColumnInfo(name = "training_rating")
    var trainingRate: Int = 0,

    @ColumnInfo(name = "security_rating")
    var securityRate: Int = 0,

    @ColumnInfo(name = "sustain_rating")
    var sustainRate: Int = 0,

    @ColumnInfo(name = "light_assault_rating")
    var lightAssaultRate: Int = 0,

    @ColumnInfo(name = "medium_assault__rating")
    var mediumAssaultRate: Int = 0,

    @ColumnInfo(name = "heavy_assault_rating")
    var heavyAssaultRate: Int = 0
)