package com.example.desafio2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.desafio2.R
import com.example.desafio2.solinftec_navigation.SupportScreenManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {
    private val _compositeDisposable = CompositeDisposable()
    private lateinit var _mainNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        _mainNavController = Navigation.findNavController(this, R.id.f_nav_host)

        SupportScreenManager.mainFragmentId = R.id.userListFragment

        _compositeDisposable.add(SupportScreenManager.fragmentDestination
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                if (_mainNavController.currentDestination?.id != it.fragmentId) {
                    Log.i(
                        "BUGUINHO",
                        "comitou fragment [ current -> ${_mainNavController.currentDestination?.id} - nome: ${_mainNavController.currentDestination?.label} ] -> [ fragmentId -> ${it.fragmentId} ] "
                    )
                    _mainNavController.popBackStack()
                    _mainNavController.navigate(it.fragmentId, it.extras)
                    Log.i(
                        "BUGUINHO",
                        "[ current -> ${_mainNavController.currentDestination?.id} - nome: ${_mainNavController.currentDestination?.label} ]"
                    )
                }
            }, { t ->
                t.printStackTrace()
            })
        )

    }

    override fun onBackPressed() {
        SupportScreenManager.goToMain()
    }

}