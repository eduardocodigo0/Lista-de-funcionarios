package com.example.desafio2.ui


import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.desafio2.Constants
import com.example.desafio2.R
import com.example.desafio2.data.UserModel
import com.example.desafio2.solinftec_navigation.FragmentInfo
import com.example.desafio2.solinftec_navigation.SupportScreenManager
import com.google.android.material.floatingactionbutton.FloatingActionButton


class updateUserFragment : Fragment() {


    private lateinit var cod: TextView
    private lateinit var nome: EditText
    private lateinit var complemento: EditText
    private lateinit var res1: EditText
    private lateinit var res2: EditText

    private lateinit var currentUser: UserModel
    private lateinit var mViewModel: UpdateUserFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_user, container, false)

        cod = view.findViewById(R.id.tv_codigo_update)
        nome = view.findViewById(R.id.et_update_nome)
        complemento = view.findViewById(R.id.et_update_complemento)
        res1 = view.findViewById(R.id.et_update_reservado1)
        res2 = view.findViewById(R.id.et_update_reservado2)

        currentUser = arguments?.getSerializable(Constants.bundle_key) as UserModel

        cod.text = currentUser.cod.toString()
        nome.setText(currentUser.nome)
        complemento.setText(currentUser.complemento)
        res1.setText(currentUser.reservado1)
        res2.setText(currentUser.reservado2)

        mViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(UpdateUserFragmentViewModel::class.java)
        mViewModel.dataError.observe(viewLifecycleOwner, mDataErrorObserver)
        mViewModel.isUserDeleted.observe(viewLifecycleOwner, mDeleteObserver)
        mViewModel.isUserUpdated.observe(viewLifecycleOwner, mUpdateObserver)

        view.findViewById<FloatingActionButton>(R.id.fab_delete_user).setOnClickListener(mListener)
        view.findViewById<FloatingActionButton>(R.id.fab_update_user).setOnClickListener(mListener)


        return view
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
            SupportScreenManager.goTo(FragmentInfo(R.id.userListFragment))
        }
    }

    private val mUpdateObserver = Observer<Boolean> { updated ->
        if (updated) {
            Toast.makeText(context, "O funcionário foi atualizado com sucesso!", Toast.LENGTH_SHORT)
                .show()
            mViewModel.resetValues()
            SupportScreenManager.goTo(FragmentInfo(R.id.userListFragment))
        }
    }

    private val mListener = View.OnClickListener { view ->
        when (view.id) {

            R.id.fab_update_user -> {

                if (nome.text.isNullOrEmpty() ||
                    complemento.text.isNullOrEmpty() ||
                    res1.text.isNullOrEmpty() ||
                    res2.text.isNullOrEmpty()
                ) {
                    Toast.makeText(context, "Não deixe campos vazios!", Toast.LENGTH_LONG).show()
                } else if (
                    nome.text.toString().toUpperCase() == currentUser.nome &&
                    complemento.text.toString() == currentUser.complemento &&
                    res1.text.toString() == currentUser.reservado1 &&
                    res2.text.toString() == currentUser.reservado2
                ) {
                    Toast.makeText(context, "Nenhuma modificação foi detectada!", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val newUser = UserModel(
                        nome = nome.text.toString().toUpperCase(),
                        complemento = complemento.text.toString(),
                        reservado1 = res1.text.toString(),
                        reservado2 = res2.text.toString(),
                        cod = currentUser.cod
                    )
                    mViewModel.updateUserInDB(newUser)
                }
            }

            R.id.fab_delete_user -> {
                //mViewModel.deleteUserFromDB(currentUser)
                callAlert()?.show()
            }
        }
    }

    fun callAlert() : AlertDialog?{
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