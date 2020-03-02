package com.example.testapp.ui.battle.actions.action

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class BattleActionActionFragmentViewModel : RxViewModel() {

    private val dbm: DBModelImpl by inject()

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