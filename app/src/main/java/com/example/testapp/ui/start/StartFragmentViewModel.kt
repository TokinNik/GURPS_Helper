package com.example.testapp.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.db.entity.Character
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class StartFragmentViewModel: RxViewModel() {

    private val dbm: DBModelImpl by inject()

    val getAllCharactersComplete: LiveData<List<Character>>
        get() = getAllCharactersEvent

    val error: LiveData<Throwable>
        get() = errorEvent

    val deleteComplete: LiveData<Boolean>
        get() = deleteCompleteEvent

    val addComplete: LiveData<Boolean>
        get() = addCompleteEvent

    val getLastCharacterIdComplete: LiveData<Int>
        get() = getLastCharacterIdEvent

    private var addCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var getAllCharactersEvent: MutableLiveData<List<Character>> = MutableLiveData()

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var deleteCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var getLastCharacterIdEvent: MutableLiveData<Int> = MutableLiveData()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
    }

    fun getAllCharacters() {
        dbm.getDB().characterDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                getAllCharactersEvent.value = it
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
            .let(compositeDisposable::add)
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
            .let(compositeDisposable::add)
    }

    fun updateCharacter(character: Character) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.characterDao().update(character)
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
            .let(compositeDisposable::add)
    }

    fun getLastCharacterId() {
        dbm.getDB().characterDao().getLastCharacterId()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    getLastCharacterIdEvent.value = it
                },
                {
                    errorEvent.value = it
                }
            ).let(compositeDisposable::add)
    }

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        deleteCompleteEvent =  MutableLiveData()
        getAllCharactersEvent =  MutableLiveData()
        addCompleteEvent = MutableLiveData()
        getLastCharacterIdEvent = MutableLiveData()
    }
}