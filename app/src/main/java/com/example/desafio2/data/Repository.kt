package com.example.desafio2.data

import android.app.Application
import android.net.Uri
import com.example.desafio2.util.FileAccessUtil
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.Exception

class Repository(val application: Application) {

    private val room = UserRoomDatabase.getDataBaseInstance(application)


    //ROOM METHODS

    fun getAllUsersFromDB(): Single<List<UserModel>> {
        return room.UserDao().getAllUsers()
    }

    fun getAllUsersByNameFromDB(): Single<List<UserModel>> {
        return room.UserDao().getAllUsersByName()
    }

    fun getAllUsersByDecCodeFromDB(): Single<List<UserModel>> {
        return room.UserDao().getAllUsersByDecCode()
    }

    fun getOneUserFromDB(cod: Long): Single<UserModel> {
        return room.UserDao().getOneUser(cod)
    }

    fun insertManyInDB(list: List<UserModel>): Single<List<Long>> {
        return room.UserDao().insertMany(list)
    }

    fun insertOneInDB(user: UserModel): Single<Long> {
        return room.UserDao().insertOne(user)
    }

    fun deleteFromDB(user: UserModel): Single<Int> {
        return room.UserDao().deleteUser(user)
    }

    fun updateInDB(user: UserModel): Single<Int> {
        return room.UserDao().updateUser(user)
    }


    //TXT FILE METHODS

    fun getAllUserFromTXT(): MutableList<UserModel> {
        return FileAccessUtil.getUsersFromFile(application)
    }

    fun saveAllUsersInTXT(list: MutableList<UserModel>) {
        return FileAccessUtil.saveUsersInFile(application, list)
    }

    fun getDataFromUploadedFile(uri: Uri) = Observable.create<Boolean>{ emmiter ->

        try{

            val success = FileAccessUtil.getDataFromUploadedFile(uri, application)
            emmiter.onNext(success)

        }catch (err: Exception){
            emmiter.onError(err)
        }
    }

}