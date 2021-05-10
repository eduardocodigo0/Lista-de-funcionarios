package com.example.desafio2.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.desafio2.data.Repository
import com.example.desafio2.data.UserModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewUserFragmentViewModel(application: Application):AndroidViewModel(application), LifecycleObserver {

    private val cd = CompositeDisposable()
    private val repo = Repository(application)

    private val mDataError: MutableLiveData<Boolean> = MutableLiveData()
    val dataError: LiveData<Boolean> get() = mDataError

    private val mIsUserSaved: MutableLiveData<Boolean> = MutableLiveData()
    val isUserSaved: LiveData<Boolean> get() = mIsUserSaved

    fun saveNewUser(user: UserModel){

        cd.add(repo.insertOneInDB(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    getAllUsersFromDBAndUpdateTXTFile()
                },
                {   err ->
                    mDataError.postValue(true)
                    Log.d(
                        "BUGUINHO",
                        err.message ?: "ERRO EM -> NewUserFragmentViewModel.saveNewUser"
                    )
                }
            ))

    }

    fun getAllUsersFromDBAndUpdateTXTFile(){
        cd.add(repo.getAllUsersFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    updateTXTfile(list as MutableList<UserModel>)
                }, //OnNext
                { err ->
                    Log.d(
                        "BUGUINHO",
                        err.message ?: "ERRO EM -> NewUserFragmentViewModel.getAllUsersFromDBAndUpdateTXTFile"
                    )
                    mDataError.postValue(true)
                } //OnError
            )
        )
    }

    fun updateTXTfile(list: MutableList<UserModel>){

        cd.add(
            Observable.just(repo.saveAllUsersInTXT(list))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    mIsUserSaved.postValue(true)
                }
        )


    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resetValues() {
        mDataError.value = false
        mIsUserSaved.value = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun dispose() {
        cd.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }


}