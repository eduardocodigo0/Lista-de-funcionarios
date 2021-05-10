package com.example.desafio2.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.desafio2.Constants

@Database(entities = [UserModel::class], version = 2)
abstract class UserRoomDatabase : RoomDatabase() {

    abstract fun UserDao(): UserDao

    companion object {

        private var db_instance: UserRoomDatabase? = null

        fun getDataBaseInstance(application: Application): UserRoomDatabase {

            if (db_instance != null) {
                return db_instance!!
            }

            db_instance = Room.databaseBuilder(
                application,
                UserRoomDatabase::class.java,
                Constants.database_name
            ).fallbackToDestructiveMigration()
                .build()

            return db_instance!!
        }

    }

}