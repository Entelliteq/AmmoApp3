package com.intelliteq.fea.ammocalculator.persistence.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weapon_ammo_table")
data class WeaponAmmo(
    @PrimaryKey(autoGenerate = true)
    var ammoId: Long = 0L,

    @ColumnInfo(name = "weapon_for_ammo")
    var weaponId: Long = 0L,

    @ColumnInfo(name = "dodic_for_ammo")
    var DODIC: String? = "",

    @ColumnInfo(name = "ammo_description")
    var ammoDescription: String? = "",

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
    var ammoType: String? = ""

) //: Ammo(), Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readLong(),
//        parcel.readLong(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readInt(),
//        parcel.readInt(),
//        parcel.readInt(),
//        parcel.readInt(),
//        parcel.readInt(),
//        parcel.readInt(),
//        parcel.readString()
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeLong(ammoId)
//        parcel.writeLong(weaponId)
//        parcel.writeString(DODIC)
//        parcel.writeString(ammoDescription)
//        parcel.writeInt(trainingRate)
//        parcel.writeInt(securityRate)
//        parcel.writeInt(sustainRate)
//        parcel.writeInt(lightAssaultRate)
//        parcel.writeInt(mediumAssaultRate)
//        parcel.writeInt(heavyAssaultRate)
//        parcel.writeString(ammoType)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<WeaponAmmo> {
//        override fun createFromParcel(parcel: Parcel): WeaponAmmo {
//            return WeaponAmmo(parcel)
//        }
//
//        override fun newArray(size: Int): Array<WeaponAmmo?> {
//            return arrayOfNulls(size)
//        }
//    }
//
//}
//
