package com.intelliteq.fea.ammocalculator.persistence.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "component_ammo_table")
data class ComponentAmmo(
    @PrimaryKey(autoGenerate = true)
    var componentAmmoId: Long =0L,

    @ColumnInfo(name = "component_id_for_component_ammo")
    var componentId: Long = 0,

    @ColumnInfo(name = "component_ammo_type_id")
    var componentAmmoTypeID: String? = "",

    @ColumnInfo(name = "component_ammo_description")
    var componentAmmoDescription: String? = "",

    @ColumnInfo(name="component_ammo_dodic")
    var componentAmmoDODIC: String? = "",

    @ColumnInfo(name= "weapon_id_for_component_ammo")
    var weaponIdComponentAmmo: Long = 0L

) //: Ammo(), Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readLong(),
//        parcel.readLong(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readLong()
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeLong(componentAmmoId)
//        parcel.writeLong(componentId)
//        parcel.writeString(componentAmmoTypeID)
//        parcel.writeString(componentAmmoDescription)
//        parcel.writeString(componentAmmoDODIC)
//        parcel.writeLong(weaponIdComponentAmmo)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<ComponentAmmo> {
//        override fun createFromParcel(parcel: Parcel): ComponentAmmo {
//            return ComponentAmmo(parcel)
//        }
//
//        override fun newArray(size: Int): Array<ComponentAmmo?> {
//            return arrayOfNulls(size)
//        }
//    }
//
//
//}