package com.example.desafio2.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2.R
import com.example.desafio2.adapter.UserRecyclerViewAdapter
import com.example.desafio2.data.UserModel
import com.example.desafio2.solinftec_navigation.FragmentInfo
import com.example.desafio2.solinftec_navigation.SupportScreenManager
import com.example.desafio2.util.UploadStateManager
import com.example.desafio2.util.UploadStates
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserListFragment : Fragment() {

    private lateinit var mViewModel: UserListFragmentViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(UserListFragmentViewModel::class.java)

        view.findViewById<FloatingActionButton>(R.id.fab_new_user).setOnClickListener(mListener)
        view.findViewById<FloatingActionButton>(R.id.fab_import_users).setOnClickListener(mListener)
        view.findViewById<Spinner>(R.id.spinner_order).onItemSelectedListener = mSpinnerListener


        mProgressBar = view.findViewById(R.id.pb_loading)

        mRecyclerView = view.findViewById(R.id.rv_list_user)
        mRecyclerView.adapter = UserRecyclerViewAdapter(listOf())

        viewLifecycleOwner.lifecycle.addObserver(mViewModel)
        mViewModel.userList.observe(viewLifecycleOwner, mListObserver)
        mViewModel.dataError.observe(viewLifecycleOwner, mDataErrorObserver)
        mViewModel.isUploadSuccess.observe(viewLifecycleOwner, mUploadSuccess)


        return view
    }

    private val mListObserver = Observer<List<UserModel>>{ userList ->
        mRecyclerView.apply {
            adapter = UserRecyclerViewAdapter(userList)
            mProgressBar.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
        }

    }

    private val mDataErrorObserver = Observer<Boolean>{ error ->
        if(error) {
            Toast.makeText(context, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            mViewModel.clearError()
        }
    }

    private val mUploadSuccess = Observer<UploadStates>{ state ->
        if(state != UploadStates.IDLE){
            if(state == UploadStates.SUCCESS) {
                Toast.makeText(context, "Os dados foram carregados com sucesso", Toast.LENGTH_SHORT).show()
                mViewModel.clearError()
            }else{
                Toast.makeText(context, "Nenhum funcionário foi encontrado no arquivo", Toast.LENGTH_SHORT).show()
                mViewModel.clearError()
            }

            mViewModel.setUploadIdle()
            Navigation.findNavController(requireActivity(), R.id.f_nav_host).popBackStack()
            SupportScreenManager.goTo(FragmentInfo(R.id.userListFragment))
        }
    }

    private val mListener = View.OnClickListener { view ->
        when(view.id){

            R.id.fab_new_user -> {
                val fragmentInfo = FragmentInfo(R.id.newUserFragment)
                SupportScreenManager.goTo(fragmentInfo)
            }

            R.id.fab_import_users -> {
                if(UploadStateManager.getState() == UploadStates.IDLE) {
                    val intent = Intent()
                        .setType("text/plain")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT)

                    startActivityForResult(Intent.createChooser(intent, "Procure o arquivo"), 111)
                }

            }
        }
    }

    private val mSpinnerListener = object: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            mRecyclerView.visibility = View.GONE
            mProgressBar.visibility = View.VISIBLE

            when(position){
                0 -> mViewModel.getUsersFromDB()
                1 -> mViewModel.getUsersByDecCodeFromDB()
                2 -> mViewModel.getUsersByNameFromDB()
            }

        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data

            selectedFile?.also{ uri ->
                mViewModel.getDataFromUploadedFile(uri)
            }


        }else{
            Navigation.findNavController(requireActivity(), R.id.f_nav_host).popBackStack()
            SupportScreenManager.goTo(FragmentInfo(R.id.userListFragment))
        }

    }



}