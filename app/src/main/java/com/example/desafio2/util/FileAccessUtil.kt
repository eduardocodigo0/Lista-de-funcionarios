package com.example.desafio2.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.desafio2.Constants
import com.example.desafio2.data.UserModel
import java.io.*
import java.nio.charset.Charset


class FileAccessUtil {

    companion object {
        private const val FILE_NAME = Constants.file_name

        fun getUsersFromFile(context: Context): MutableList<UserModel> {
            val userList = mutableListOf<UserModel>()

            val downloads = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloads, FILE_NAME)

            if (file.exists()) {
                file.readLines(Charset.forName("ISO-8859-1")).forEach { text ->
                    text.split(";").let {

                        val newUser = UserModel(it[0].toLong(), it[1], it[2], it[3], it[4])
                        userList.add(newUser)

                    }
                }
            } else {
                file.createNewFile()
            }
            return userList
        }

        fun saveUsersInFile(context: Context, userList: MutableList<UserModel>) {

            val downloads = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloads, FILE_NAME)

            file.writeText(getLinesFromUserModelList(userList), Charset.forName("ISO-8859-1"))
        }

        private fun getLinesFromUserModelList(userList: MutableList<UserModel>): String {

            var linesList = ""

            userList.forEach {
                linesList += "${it.toString()}\n"
            }

            return linesList
        }

        fun getDataFromUploadedFile(uri: Uri, context: Context): Boolean {

            val regex = "\\d*;[^0-9]*;[^0-9]*;\\d*;\\d*".toRegex()
            val userList = mutableListOf<UserModel>()

            context.contentResolver.openInputStream(uri).use { inputStream ->
                BufferedReader(
                    InputStreamReader(inputStream, Charset.forName("ISO-8859-1"))
                ).use { reader ->

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {

                        line?.also { fileLine ->
                            if (fileLine.matches(regex)) {
                                fileLine.split(";").let {

                                    val newUser =
                                        UserModel(it[0].toLong(), it[1], it[2], it[3], it[4])
                                    userList.add(newUser)

                                }
                            }
                        }
                    }

                }
            }

            return if (userList.isNotEmpty()) {
                val oldUsers = getUsersFromFile(context)


                val listToSave = (oldUsers + userList.filter { newUser ->
                    newUser !in oldUsers
                }) as MutableList<UserModel>

                saveUsersInFile(context, listToSave)

                true
            } else {
                false
            }

        }

    }


}





