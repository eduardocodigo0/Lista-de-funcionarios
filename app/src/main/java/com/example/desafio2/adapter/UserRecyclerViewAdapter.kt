package com.example.desafio2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2.R
import com.example.desafio2.data.UserModel
import com.example.desafio2.databinding.UserListItemBinding


class UserRecyclerViewAdapter(val users: List<UserModel>, val listener: (UserModel) -> Unit) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list_item, parent, false)

        return UserViewHolder(view)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.bind(users[position], listener)
    }

    override fun getItemCount(): Int {
        return users.size
    }

}

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = UserListItemBinding.bind(itemView)

    fun bind(user: UserModel, behavior: (UserModel) -> Unit) {


        binding.tvCodigo.text = user.cod.toString()
        binding.tvNome.text = user.nome
        binding.tvComplemento.text = user.complemento
        binding.tvReservado1.text = user.reservado1
        binding.tvReservado2.text = user.reservado2

        itemView.setOnClickListener{behavior(user)}

    }

}