package com.example.desafio2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.desafio2.Constants
import java.io.Serializable

@Entity(tableName = Constants.user_table_name)
data class UserModel(

    @PrimaryKey(autoGenerate = true)
    var cod: Long = 0,
    var nome: String = "",
    var complemento: String = "",
    var reservado1: String = "",
    var reservado2: String = ""

) : Serializable {

    override fun toString(): String {
        return "${cod};${nome};${complemento};${reservado1};${reservado2}"
    }
}