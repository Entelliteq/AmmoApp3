package com.intelliteq.fea.ammocalculator.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.intelliteq.fea.ammocalculator.persistence.daos.*
import com.intelliteq.fea.ammocalculator.persistence.models.*

@Database(
    entities = [Weapon::class, WeaponAmmo::class, Component::class, ComponentAmmo::class, SingleWeaponCalculation::class, Calculations::class],
    version = 1,
    exportSchema = false
)
abstract class AmmoRoomDatabase : RoomDatabase() {
    abstract val weaponDao: WeaponDao
    abstract val weaponAmmoDao: WeaponAmmoDao
    abstract val componentDao: ComponentDao
    abstract val componentAmmoDao: ComponentAmmoDao
    abstract val singleWeaponCalculationDao: SingleWeaponCalculationDao
    abstract val calculationsDao : CalculationsDao


    companion object {
        @Volatile
        private var INSTANCE: AmmoRoomDatabase? = null

        fun getAppDatabase(context: Context): AmmoRoomDatabase? {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AmmoRoomDatabase::class.java,
                        "ammo_db"
                    )
                        .build()
                    INSTANCE = instance
                }
                return INSTANCE
            }
        }

        fun destroyDatabase() {
            INSTANCE = null
        }
    }

}