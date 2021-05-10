package com.example.desafio2.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2.Constants
import com.example.desafio2.R
import com.example.desafio2.data.UserModel
import com.example.desafio2.solinftec_navigation.FragmentInfo
import com.example.desafio2.solinftec_navigation.SupportScreenManager


class UserRecyclerViewAdapter(val users: List<UserModel>) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list_item, parent, false)

        return UserViewHolder(view)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(
            users[position].cod,
            users[position].nome,
            users[position].complemento,
            users[position].reservado1,
            users[position].reservado2
        )
    }

    override fun getItemCount(): Int {
        return users.size
    }

}


class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mCod = itemView.findViewById<TextView>(R.id.tv_codigo)
    private val mNome = itemView.findViewById<TextView>(R.id.tv_nome)
    private val mComplemento = itemView.findViewById<TextView>(R.id.tv_complemento)
    private val mReservado1 = itemView.findViewById<TextView>(R.id.tv_reservado1)
    private val mReservado2 = itemView.findViewById<TextView>(R.id.tv_reservado2)

    fun bind(cod: Long, nome: String, complemento: String, res1: String, res2: String) {


        mCod.text = cod.toString()
        mNome.text = nome
        mComplemento.text = complemento
        mReservado1.text = res1
        mReservado2.text = res2

        itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                Constants.bundle_key,
                UserModel(cod, nome, complemento, res1, res2)
            )

            SupportScreenManager.goTo(FragmentInfo(R.id.updateUserFragment, bundle))
        }

    }

}