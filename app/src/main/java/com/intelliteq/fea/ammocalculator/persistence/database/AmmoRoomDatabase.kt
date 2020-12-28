package com.intelliteq.fea.ammocalculator.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.intelliteq.fea.ammocalculator.persistence.daos.*
import com.intelliteq.fea.ammocalculator.persistence.models.*

@Database(
    entities = [Weapon::class,  Ammo::class, Component::class, PerWeaponCalculation::class, Calculation::class],
    version = 1,
    exportSchema = false
)
abstract class AmmoRoomDatabase : RoomDatabase() {
    abstract val weaponDao: WeaponDao
    abstract val componentDao: ComponentDao
    abstract val perWeaponCalculationDao: PerWeaponCalculationDao
    abstract val calculationDao : CalculationDao
    abstract val ammoDao: AmmoDao

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