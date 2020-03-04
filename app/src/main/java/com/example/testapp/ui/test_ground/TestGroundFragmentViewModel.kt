package com.example.testapp.ui.test_ground

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.ui.RxViewModel
import toothpick.Toothpick

class TestGroundFragmentViewModel: RxViewModel() {

    val error: LiveData<Throwable>
        get() = errorEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
    }

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
    }
}