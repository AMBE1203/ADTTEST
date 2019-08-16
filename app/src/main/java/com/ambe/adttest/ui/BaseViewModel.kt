package com.ambe.adttest.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ambe.adttest.helper.Event
import com.ambe.adttest.model.Response

/**
 *  Created by AMBE on 16/8/2019 at 13:39 PM.
 */
open class BaseViewModel(app:Application) : AndroidViewModel(app) {
    val eventLoading = MutableLiveData<Event<Boolean>>()
    val eventError = MutableLiveData<Event<Boolean>>()
    val eventFailure = MutableLiveData<Event<Throwable>>()


    fun showLoading(value: Boolean) {
        eventLoading.value = Event(value)
    }


    fun showError(error: Boolean) {
        eventError.value = Event(error)
    }

    fun showFailure(throwable: Throwable) {
        eventFailure.value = Event(throwable)
    }
}