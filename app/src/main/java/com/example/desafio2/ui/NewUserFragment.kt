package com.example.desafio2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.desafio2.R
import com.example.desafio2.data.UserModel
import com.example.desafio2.solinftec_navigation.FragmentInfo
import com.example.desafio2.solinftec_navigation.SupportScreenManager
import com.google.android.material.floatingactionbutton.FloatingActionButton


class newUserFragment : Fragment() {

    private lateinit var nome: EditText
    private lateinit var complemento: EditText
    private lateinit var res1: EditText
    private lateinit var res2: EditText

    private lateinit var mViewModel: NewUserFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_user, container, false)

        nome = view.findViewById(R.id.et_nome)
        complemento = view.findViewById(R.id.et_complemento)
        res1 = view.findViewById(R.id.et_reservado1)
        res2 = view.findViewById(R.id.et_reservado2)

        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(NewUserFragmentViewModel::class.java)

        mViewModel.dataError.observe(viewLifecycleOwner, mDataErrorObserver)
        mViewModel.isUserSaved.observe(viewLifecycleOwner, mIsUserSavedObserver)

        view.findViewById<FloatingActionButton>(R.id.fab_save_new_user)
            .setOnClickListener(mListener)


        return view
    }


    private val mListener = View.OnClickListener { view ->
        when (view.id) {

            R.id.fab_save_new_user -> {

                if (nome.text.isNullOrEmpty() || complemento.text.isNullOrEmpty() || res1.text.isNullOrEmpty() || res2.text.isNullOrEmpty()) {
                    Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_LONG).show()
                } else {
                    val newUser = UserModel(
                        nome = nome.text.toString().toUpperCase(),
                        complemento = complemento.text.toString(),
                        reservado1 = res1.text.toString(),
                        reservado2 = res2.text.toString()
                    )

                    mViewModel.saveNewUser(newUser)
                }
            }
        }
    }

    private val mDataErrorObserver = Observer<Boolean> { error ->
        if (error) {
            Toast.makeText(context, "Erro ao salvar o novo funcionário!", Toast.LENGTH_SHORT).show()
            mViewModel.resetValues()
        }
    }

    private val mIsUserSavedObserver = Observer<Boolean> { saved ->
        if (saved) {
            Toast.makeText(context, "Um novo funcionário foi adicionado!", Toast.LENGTH_SHORT).show()
            mViewModel.resetValues()
            SupportScreenManager.goTo(FragmentInfo(R.id.userListFragment))
        }
    }


}