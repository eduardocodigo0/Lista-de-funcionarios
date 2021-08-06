package com.example.desafio2.solinftec_navigation

import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class EduardoScreenManager private constructor() {

    private var mainFragmentId: Int? = null
    private var screenChageJob: Job = Job()
    private lateinit var scope: CoroutineScope
    private lateinit var fragmentDestination: MutableStateFlow<FragmentInfo>
    private var currentFragmentInfo: FragmentInfo? = null

    companion object {

        private var managerInstance: EduardoScreenManager? = null

        fun initialize(
            mainFragmentId: Int,
            scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
        ) {
            managerInstance ?: run {
                val newInstance = EduardoScreenManager()
                newInstance.mainFragmentId = mainFragmentId
                newInstance.scope = scope
                newInstance.fragmentDestination = MutableStateFlow(FragmentInfo(mainFragmentId))
                managerInstance = newInstance
            }
        }

        fun goTo(fragmentInfo: FragmentInfo) = managerInstance?.also {
            it.goTo(fragmentInfo)
        }

        fun goToMain() = managerInstance?.also { it.goToMain() }

        fun stopAndClean() {
            managerInstance?.screenChageJob?.cancel()
            managerInstance = null
        }

        fun startManager(navController: NavController){
            managerInstance?.start(navController) ?: throw Exception("EduardoScreenManager Not Initialized")
        }
    }

    private fun updateDestination() = currentFragmentInfo?.also {
        fragmentDestination.value = it
    }

    fun goTo(fragmentInfo: FragmentInfo) {
        if (currentFragmentInfo?.fragmentId != fragmentInfo.fragmentId) {
            currentFragmentInfo = fragmentInfo
            updateDestination()
        }
    }

    fun goToMain() {
        mainFragmentId?.also {
            currentFragmentInfo = FragmentInfo(it)
            updateDestination()
        } ?: throw Exception("mainFragment id is null")
    }

    private fun start(navController: NavController) {
        screenChageJob = scope.launch(Dispatchers.Main) {
            fragmentDestination.collect {
                ensureActive()
                try {
                    if (navController.currentDestination?.id != it.fragmentId) {

                        navController.popBackStack()
                        navController.navigate(it.fragmentId, it.extras)

                    }
                }catch (ex: Exception){ ex.printStackTrace() }
            }
        }
    }

}