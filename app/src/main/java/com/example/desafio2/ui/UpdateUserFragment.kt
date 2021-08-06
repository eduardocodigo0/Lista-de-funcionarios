package com.example.desafio2.ui


import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.desafio2.Constants
import com.example.desafio2.R
import com.example.desafio2.data.UserModel
import com.example.desafio2.databinding.FragmentUpdateUserBinding
import com.example.desafio2.solinftec_navigation.EduardoScreenManager
import com.example.desafio2.solinftec_navigation.FragmentInfo



class updateUserFragment : Fragment() {


    private var _binding: FragmentUpdateUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentUser: UserModel
    private lateinit var mViewModel: UpdateUserFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateUserBinding.inflate(inflater, container, false)
        currentUser = arguments?.getSerializable(Constants.bundle_key) as UserModel

        binding.tvCodigoUpdate.text = currentUser.cod.toString()
        binding.etUpdateNome.setText(currentUser.nome)
        binding.etUpdateComplemento.setText(currentUser.complemento)
        binding.etUpdateReservado1.setText(currentUser.reservado1)
        binding.etUpdateReservado2.setText(currentUser.reservado2)

        mViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(UpdateUserFragmentViewModel::class.java)
        mViewModel.dataError.observe(viewLifecycleOwner, mDataErrorObserver)
        mViewModel.isUserDeleted.observe(viewLifecycleOwner, mDeleteObserver)
        mViewModel.isUserUpdated.observe(viewLifecycleOwner, mUpdateObserver)

        binding.fabDeleteUser.setOnClickListener(mListener)
        binding.fabUpdateUser.setOnClickListener(mListener)


        return binding.root
    }

    private val mDataErrorObserver = Observer<Boolean> { error ->
        if (error) {
            Toast.makeText(context, "Erro ao modificar funcionário!", Toast.LENGTH_SHORT).show()
            mViewModel.resetValues()
        }
    }

    private val mDeleteObserver = Observer<Boolean> { deleted ->
        if (deleted) {
            Toast.makeText(context, "O funcionário foi deletado com sucesso!", Toast.LENGTH_SHORT)
                .show()
            mViewModel.resetValues()
            EduardoScreenManager.goTo(FragmentInfo(R.id.userListFragment))
        }
    }

    private val mUpdateObserver = Observer<Boolean> { updated ->
        if (updated) {
            Toast.makeText(context, "O funcionário foi atualizado com sucesso!", Toast.LENGTH_SHORT)
                .show()
            mViewModel.resetValues()
            EduardoScreenManager.goTo(FragmentInfo(R.id.userListFragment))
        }
    }

    private val mListener = View.OnClickListener { view ->
        when (view.id) {

            R.id.fab_update_user -> {

                if (binding.etUpdateNome.text.isNullOrEmpty() ||
                    binding.etUpdateComplemento.text.isNullOrEmpty() ||
                    binding.etUpdateReservado1.text.isNullOrEmpty() ||
                    binding.etUpdateReservado2.text.isNullOrEmpty()
                ) {
                    Toast.makeText(context, "Não deixe campos vazios!", Toast.LENGTH_LONG).show()
                } else if (
                    binding.etUpdateNome.text.toString().toUpperCase() == currentUser.nome &&
                    binding.etUpdateComplemento.text.toString() == currentUser.complemento &&
                    binding.etUpdateReservado1.text.toString() == currentUser.reservado1 &&
                    binding.etUpdateReservado2.text.toString() == currentUser.reservado2
                ) {
                    Toast.makeText(context, "Nenhuma modificação foi detectada!", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val newUser = UserModel(
                        nome = binding.etUpdateNome.text.toString().toUpperCase(),
                        complemento = binding.etUpdateComplemento.text.toString(),
                        reservado1 = binding.etUpdateReservado1.text.toString(),
                        reservado2 = binding.etUpdateReservado2.text.toString(),
                        cod = currentUser.cod
                    )
                    mViewModel.updateUserInDB(newUser)
                }
            }

            R.id.fab_delete_user -> {
                callAlert()?.show()
            }
        }
    }

    fun callAlert(): AlertDialog? {
        return activity?.let {

            val alertBuilder = AlertDialog.Builder(it)

            alertBuilder.apply {
                setMessage("Deseja excluir este funcionário?")

                setPositiveButton("SIM",
                    DialogInterface.OnClickListener { dialog, id ->
                        mViewModel.deleteUserFromDB(currentUser)
                    }
                )
                setNegativeButton("NÃO",
                    DialogInterface.OnClickListener { dialog, id ->
                        Toast.makeText(
                            context,
                            "O funcionário não foi deletado!",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
            }
            alertBuilder.create()
        }


    }


}