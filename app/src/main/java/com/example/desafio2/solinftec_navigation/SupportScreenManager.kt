package com.example.desafio2.solinftec_navigation

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

object SupportScreenManager {
    var mainFragmentId: Int? = null

    private val _fragmentDestination: Relay<FragmentInfo> = PublishRelay.create()
    private var _currentFragmentInfo: FragmentInfo? = null
    val currentFragmentInfo get() = _currentFragmentInfo

    fun goTo(fragmentInfo: FragmentInfo) {
        if (_fragmentDestination.hasObservers()) {
            _fragmentDestination.accept(fragmentInfo)
            _currentFragmentInfo = fragmentInfo
        }
    }

    fun goToMain() {
        mainFragmentId?.also {
            if (_fragmentDestination.hasObservers()) {
                _fragmentDestination.accept(FragmentInfo(it))
                _currentFragmentInfo = FragmentInfo(it)
            }
        } ?: run {
            throw Exception("mainFragment id is null")
        }
    }

    val fragmentDestination: Flowable<FragmentInfo>
        get() = _fragmentDestination.toFlowable(BackpressureStrategy.LATEST)
}