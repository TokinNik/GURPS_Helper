package com.example.testapp.ui.battle.actions.attack_range

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.ui.RxViewModel
import toothpick.Toothpick


class BattleActionAttackRangeFragmentViewModel : RxViewModel() {

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