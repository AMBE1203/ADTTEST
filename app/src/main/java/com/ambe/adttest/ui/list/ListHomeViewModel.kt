package com.ambe.adttest.ui.list

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.ambe.adttest.helper.Const
import com.ambe.adttest.interfaces.NetworkService
import com.ambe.adttest.model.Response
import com.ambe.adttest.ui.BaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

/**
 *  Created by AMBE on 16/8/2019 at 9:09 AM.
 */
class ListHomeViewModel(var app: Application) : BaseViewModel(app) {


    private val listHomes = MutableLiveData<Response>()
    private val disposables = CompositeDisposable()
    private var retryCompletable: Completable? = null

    fun getLishHomes(): MutableLiveData<Response> {
        return listHomes;
    }

    fun listIsEmpty(): Boolean {
        return listHomes.value?.homes?.isEmpty() ?: true
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    init {
        loadListHomes()
    }

    fun retry() {
        if (retryCompletable != null) {
            disposables.add(
                    retryCompletable!!
                            .subscribe()
            )
        }
    }


    private fun loadListHomes() {
        disposables.add(NetworkService.getService(app, Const.TOKEN).getHomes().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
            showLoading(true)
            showError(false)
        }.doFinally { showLoading(false) }.subscribe({


            listHomes.postValue(it)
            showError(false)


        }, {
            showError(true)
            setRetry(Action { loadListHomes() })

            showFailure(it)

        }))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}