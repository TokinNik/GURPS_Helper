package com.example.testapp.ui.battle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.db.SkillsAndCharacterOnSt
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill
import com.example.testapp.di.DBModelImpl
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class BattleFragmentViewModel: ViewModel() {

    private val dbm: DBModelImpl by inject()

    val characters: LiveData<List<Character>>
        get() = charactersEvent

    val error: LiveData<Throwable>
        get() = errorEvent

    private var charactersEvent: MutableLiveData<List<Character>> = MutableLiveData()

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()


    private val compositeDisposable = CompositeDisposable()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
    }

    fun getItems()
    {
        dbm.getDB().characterDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                charactersEvent.value = it
            }.let(compositeDisposable::add)
    }

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        charactersEvent =  MutableLiveData()
    }
}