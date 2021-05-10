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

enum class Operation{
    DELETE, UPDATE
}

class UpdateUserFragmentViewModel(application: Application): AndroidViewModel(application), LifecycleObserver {

    private val cd = CompositeDisposable()
    private val repo = Repository(application)

    private val mDataError: MutableLiveData<Boolean> = MutableLiveData()
    val dataError: LiveData<Boolean> get() = mDataError

    private val mIsUserUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val isUserUpdated: LiveData<Boolean> get() = mIsUserUpdated

    private val mIsUserDeleted: MutableLiveData<Boolean> = MutableLiveData()
    val isUserDeleted: LiveData<Boolean> get() = mIsUserDeleted


    fun updateUserInDB(userModel: UserModel){
        cd.add(repo.updateInDB(userModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    getAllUsersFromDBAndUpdateTXTFile(Operation.UPDATE)
                },
                { err ->

                    mDataError.postValue(true)
                    Log.d(
                        "BUGUINHO",
                        err.message ?: "ERRO EM -> UpdateUserFragmentViewModel.updateUserInDB"
                    )
                }
            ))
    }


    fun deleteUserFromDB(userModel: UserModel){
        cd.add(repo.deleteFromDB(userModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    getAllUsersFromDBAndUpdateTXTFile(Operation.DELETE)
                },
                { err ->

                    mDataError.postValue(true)
                    Log.d(
                        "BUGUINHO",
                        err.message ?: "ERRO EM -> UpdateUserFragmentViewModel.deleteUserFromDB"
                    )
                }
            ))
    }


    fun getAllUsersFromDBAndUpdateTXTFile(op: Operation){
        cd.add(repo.getAllUsersFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    updateTXTfile(list as MutableList<UserModel>, op)
                }, //OnNext
                { err ->
                    Log.d(
                        "BUGUINHO",
                        err.message ?: "ERRO EM -> UpdateUserFragmentViewModel.getAllUsersFromDBAndUpdateTXTFile"
                    )
                    mDataError.postValue(true)
                } //OnError
            )
        )
    }

    fun updateTXTfile(list: MutableList<UserModel>, op: Operation){

        cd.add(
            Observable.just(repo.saveAllUsersInTXT(list))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{

                    when(op){

                        Operation.DELETE -> {
                            mIsUserDeleted.postValue(true)
                        }

                        Operation.UPDATE -> {
                            mIsUserUpdated.postValue(true)
                        }
                    }

                }
        )

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resetValues() {
        mDataError.value = false
        mIsUserDeleted.value = false
        mIsUserUpdated.value = false
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