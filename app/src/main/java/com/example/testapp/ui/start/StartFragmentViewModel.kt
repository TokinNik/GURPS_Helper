package com.example.testapp.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.db.SkillsAndCharacterOnSt
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class StartFragmentViewModel: RxViewModel() {

    private val dbm: DBModelImpl by inject()

    val characters: LiveData<List<Character>>
        get() = charactersEvent

    val error: LiveData<Throwable>
        get() = errorEvent

    val deleteComplete: LiveData<Boolean>
        get() = deleteCompleteEvent

    val addComplete: LiveData<Boolean>
        get() = addCompleteEvent

    private var addCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var charactersEvent: MutableLiveData<List<Character>> = MutableLiveData()

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var deleteCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

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

    fun deleteCharacter(character: Character) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.characterDao().delete(character)
            emitter.onComplete()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                errorEvent.value = it
            }
            .doOnComplete {
                deleteCompleteEvent.value = true
            }
            .subscribe()
    }

    fun addCharacter(character: Character){
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.characterDao().insert(character)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                errorEvent.value = it
            }
            .doOnComplete {
                addCompleteEvent.value = true
            }
            .subscribe()
    }

    fun addSkill(skill: Skill) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.skillDao().insert(skill)
            emitter.onComplete()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                errorEvent.value = it
            }
            .doOnComplete {
            }
            .subscribe()
    }

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        deleteCompleteEvent =  MutableLiveData()
        charactersEvent =  MutableLiveData()
        addCompleteEvent = MutableLiveData()
    }
}