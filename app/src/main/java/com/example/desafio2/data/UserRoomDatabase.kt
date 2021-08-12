package com.example.desafio2.data

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.desafio2.Constants

@Database(entities = [UserModel::class], version = 2)
abstract class UserRoomDatabase : RoomDatabase() {

    abstract fun UserDao(): UserDao

    companion object {

        private var db_instance: UserRoomDatabase? = null

        fun getDataBaseInstance(context: Context): UserRoomDatabase {

            db_instance?.let {
                return it
            }

            db_instance.run {
               val newDatabaseInstance  =  Room.databaseBuilder(
                    context,
                    UserRoomDatabase::class.java,
                    Constants.database_name
                ).fallbackToDestructiveMigration()
                    .build()

                db_instance = newDatabaseInstance
                return newDatabaseInstance
            }

        }

    }

}