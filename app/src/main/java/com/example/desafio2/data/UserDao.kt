package com.example.desafio2.data

import androidx.room.Insert
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import io.reactivex.Single

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(user: UserModel): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMany(users: List<UserModel>): Single<List<Long>>

    @Query("SELECT * FROM users")
    fun getAllUsers(): Single<List<UserModel>>

    @Query("SELECT * FROM users ORDER BY nome ASC")
    fun getAllUsersByName(): Single<List<UserModel>>

    @Query("SELECT * FROM users ORDER BY cod DESC")
    fun getAllUsersByDecCode(): Single<List<UserModel>>

    @Query("SELECT * FROM users WHERE cod = :cod")
    fun getOneUser(cod: Long): Single<UserModel>

    @Delete
    fun deleteUser(user: UserModel): Single<Int>

    @Update
    fun updateUser(user: UserModel): Single<Int>

}