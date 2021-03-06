package com.example.desafio2.ui

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.desafio2.data.Repository
import com.example.desafio2.data.UserModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class UserListFragmentViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    private var mUsersList: MutableLiveData<List<UserModel>> = MutableLiveData()
    val userList: LiveData<List<UserModel>> get() = mUsersList

    private val mDataError: MutableLiveData<Boolean> = MutableLiveData()
    val dataError: LiveData<Boolean> get() = mDataError

    private val mIsUploadSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isUploadSuccess: LiveData<Boolean> get() = mIsUploadSuccess


    private val cd = CompositeDisposable()
    private val repo = Repository(application)


    init {
        clearError()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getDataFromFile() {

        cd.add(
            Observable.just(repo.getAllUserFromTXT())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe { list ->
                    saveFileDataOnDatabase(list)
                }
        )
    }


    fun saveFileDataOnDatabase(users: MutableList<UserModel>) {
        cd.add(repo.insertManyInDB(users)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    getUsersFromDB()
                },
                { err ->
                    Log.d(
                        "BUGUINHO",
                        err.message ?: "ERRO EM -> UserListFragmentViewModel.saveFileDataOnDatabase"
                    )
                    mDataError.postValue(true)
                }
            )
        )

    }

    fun getUsersFromDB() {
        cd.add(repo.getAllUsersFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    mUsersList.postValue(list)
                }, //OnNext
                { err ->
                    Log.d(
                        "BUGUINHO",
                        err.message ?: "ERRO EM -> UserListFragmentViewModel.getUsersFromDB"
                    )
                    mDataError.postValue(true)
                } //OnError
            )
        )

    }

    fun getUsersByNameFromDB() {
        cd.add(repo.getAllUsersByNameFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    mUsersList.postValue(list)
                }, //OnNext
                { err ->
                    Log.d(
                        "BUGUINHO",
                        err.message ?: "ERRO EM -> UserListFragmentViewModel.getUsersByNameFromDB"
                    )
                    mDataError.postValue(true)
                } //OnError
            )
        )

    }

    fun getUsersByDecCodeFromDB() {
        cd.add(repo.getAllUsersByDecCodeFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    mUsersList.postValue(list)
                }, //OnNext
                { err ->
                    Log.d(
                        "BUGUINHO",
                        err.message
                            ?: "ERRO EM -> UserListFragmentViewModel.getUsersByDecCodeFromDB"
                    )
                    mDataError.postValue(true)
                } //OnError
            )
        )

    }

    fun getDataFromUploadedFile(uri: Uri) {

        cd.add(repo.getDataFromUploadedFile(uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                Log.d("BUGUINHO", " ----> FINALMENTE")
                mIsUploadSuccess.postValue(res)
            },
                { err ->
                    Log.d(
                        "BUGUINHO",
                        err.message
                            ?: "ERRO EM -> UserListFragmentViewModel.getDataFromUploadedFile"
                    )
                    mIsUploadSuccess.postValue(false)
                }
            )
        )
    }


    fun clearError() {
        mDataError.value = false
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun dispose() {
        cd.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        cd.clear()
    }

}