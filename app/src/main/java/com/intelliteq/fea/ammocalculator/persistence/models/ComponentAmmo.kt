package com.intelliteq.fea.ammocalculator.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "component_ammo_table")
class ComponentAmmo(
    @PrimaryKey(autoGenerate = true)
    var componentAmmoId: Long =0L,

    @ColumnInfo(name = "component_id_for_component_ammo")
    var componentId: Int = 0,

    @ColumnInfo(name = "component_ammo_type_id")
    var componentAmmoTypeID: String = "",

    @ColumnInfo(name = "component_ammo_description")
    var componentAmmoDescription: String = "",

    @ColumnInfo(name="component_ammo_dodic")
    var componentAmmoDODIC: String = ""

) {


}