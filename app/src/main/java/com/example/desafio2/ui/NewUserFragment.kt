package com.example.desafio2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.desafio2.R
import com.example.desafio2.data.UserModel
import com.example.desafio2.databinding.FragmentNewUserBinding
import com.example.desafio2.solinftec_navigation.FragmentInfo
import com.example.desafio2.solinftec_navigation.SupportScreenManager


class newUserFragment : Fragment() {

    private lateinit var mViewModel: NewUserFragmentViewModel
    private var _binding: FragmentNewUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewUserBinding.inflate(inflater, container, false)


        mViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(NewUserFragmentViewModel::class.java)

        mViewModel.dataError.observe(viewLifecycleOwner, mDataErrorObserver)
        mViewModel.isUserSaved.observe(viewLifecycleOwner, mIsUserSavedObserver)

        binding.fabSaveNewUser.setOnClickListener(mListener)


        return binding.root
    }


    private val mListener = View.OnClickListener { view ->
        when (view.id) {

            R.id.fab_save_new_user -> {

                if (binding.etNome.text.isNullOrEmpty() ||
                    binding.etComplemento.text.isNullOrEmpty() ||
                    binding.etReservado1.text.isNullOrEmpty() ||
                    binding.etReservado2.text.isNullOrEmpty()
                ) {
                    Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_LONG).show()
                } else {
                    val newUser = UserModel(
                        nome = binding.etNome.text.toString().toUpperCase(),
                        complemento = binding.etComplemento.text.toString(),
                        reservado1 = binding.etReservado1.text.toString(),
                        reservado2 = binding.etReservado2.text.toString()
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
            Toast.makeText(context, "Um novo funcionário foi adicionado!", Toast.LENGTH_SHORT)
                .show()
            mViewModel.resetValues()
            SupportScreenManager.goTo(FragmentInfo(R.id.userListFragment))
        }
    }


}