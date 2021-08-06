package com.example.desafio2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.desafio2.R
import com.example.desafio2.solinftec_navigation.EduardoScreenManager


class MainActivity : AppCompatActivity() {
    private lateinit var _mainNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        _mainNavController = Navigation.findNavController(this, R.id.f_nav_host)

        EduardoScreenManager.initialize(R.id.userListFragment)
        EduardoScreenManager.startManager(_mainNavController)

    }

    override fun onBackPressed() {
        EduardoScreenManager.goToMain()
    }

    override fun onDestroy() {
        super.onDestroy()
        EduardoScreenManager.stopAndClean()
    }

}