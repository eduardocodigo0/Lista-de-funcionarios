package com.example.desafio2.util

import androidx.lifecycle.MutableLiveData

class UploadStateManager private constructor() {

    companion object {

        private var currentUploadState: MutableLiveData<UploadStates> = MutableLiveData<UploadStates>().apply { value = UploadStates.IDLE}

        fun getLiveData() = currentUploadState
        fun getState() = currentUploadState.value

        fun setState(state: UploadStates){
            currentUploadState.postValue(state)
        }
    }
}