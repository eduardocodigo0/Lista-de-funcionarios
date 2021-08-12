package com.example.desafio2.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.desafio2.Constants
import com.example.desafio2.R
import com.example.desafio2.adapter.UserRecyclerViewAdapter
import com.example.desafio2.data.UserModel
import com.example.desafio2.databinding.FragmentUserListBinding
import com.example.desafio2.solinftec_navigation.EduardoScreenManager
import com.example.desafio2.solinftec_navigation.FragmentInfo
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListFragment : Fragment() {

    private val mViewModel: UserListFragmentViewModel by viewModel()
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        binding.fabNewUser.setOnClickListener(mListener)
        binding.fabImportUsers.setOnClickListener(mListener)
        binding.spinnerOrder.onItemSelectedListener = mSpinnerListener

        viewLifecycleOwner.lifecycle.addObserver(mViewModel)
        mViewModel.userList.observe(viewLifecycleOwner, mListObserver)
        mViewModel.dataError.observe(viewLifecycleOwner, mDataErrorObserver)
        mViewModel.isUploadSuccess.observe(viewLifecycleOwner, mUploadSuccess)

        return binding.root
    }

    private val mListObserver = Observer<List<UserModel>> { userList ->

        binding.rvListUser.apply {
            adapter = UserRecyclerViewAdapter(userList) { user ->
                val bundle = Bundle()
                bundle.putSerializable(
                    Constants.bundle_key,
                    user
                )

                EduardoScreenManager.goTo(FragmentInfo(R.id.updateUserFragment, bundle))
            }
            binding.pbLoading.visibility = View.GONE
            binding.rvListUser.visibility = View.VISIBLE
        }

    }

    private val mDataErrorObserver = Observer<Boolean> { error ->
        if (error) {
            Toast.makeText(context, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            mViewModel.clearError()
        }
    }

    private val mUploadSuccess = Observer<Boolean> { success ->

        if (success) {
            Toast.makeText(context, "Os dados foram carregados com sucesso", Toast.LENGTH_SHORT)
                .show()
            mViewModel.clearError()
        } else {
            Toast.makeText(
                context,
                "Nenhum funcionário foi encontrado no arquivo",
                Toast.LENGTH_SHORT
            ).show()
            mViewModel.clearError()
        }

        Navigation.findNavController(requireActivity(), R.id.f_nav_host).popBackStack()
        EduardoScreenManager.goTo(FragmentInfo(R.id.userListFragment))

    }

    private val mListener = View.OnClickListener { view ->
        when (view.id) {

            R.id.fab_new_user -> {
                val fragmentInfo = FragmentInfo(R.id.newUserFragment)
                EduardoScreenManager.goTo(fragmentInfo)
            }

            R.id.fab_import_users -> {

                val intent = Intent()
                    .setType("text/plain")
                    .setAction(Intent.ACTION_OPEN_DOCUMENT)

                startActivityForResult(Intent.createChooser(intent, "Procure o arquivo"), 111)

            }
        }
    }

    private val mSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            binding.rvListUser.visibility = View.GONE
            binding.pbLoading.visibility = View.VISIBLE

            when (position) {
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

            selectedFile?.also { uri ->
                mViewModel.getDataFromUploadedFile(uri)
            }


        } else {
            Navigation.findNavController(requireActivity(), R.id.f_nav_host).popBackStack()
            EduardoScreenManager.goTo(FragmentInfo(R.id.userListFragment))
        }

    }

    override fun onDestroyView() {

        binding.rvListUser.adapter = null
        _binding = null

        super.onDestroyView()
    }


}