package com.example.testapp.ui.character.observe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.db.entity.Character
import com.example.testapp.di.DBModelImpl
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class CharacterFragmentViewModel: ViewModel() {

    private val dbm: DBModelImpl by inject()

    val characterById: LiveData<Character>
        get() = characterByIdEvent

    val error: LiveData<Throwable>
        get() = errorEvent

    val deleteComplete: LiveData<Boolean>
        get() = deleteCompleteEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var deleteCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var characterByIdEvent: MutableLiveData<Character> = MutableLiveData()


    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
    }

    fun getCharacterById(id: Int)
    {
        dbm.getDB().characterDao().getById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<Character>(){
                override fun onSuccess(t: Character) {
                    characterByIdEvent.value = t
                }

                override fun onError(e: Throwable) {
                    errorEvent.value = e
                }
            })
    }

    fun deleteCharacter(character: Character) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.characterDao().delete(character)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                errorEvent.value = it
            }
            .doOnComplete {
                deleteCompleteEvent.value = true
            }
            .subscribe()
    }

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        deleteCompleteEvent =  MutableLiveData()
        characterByIdEvent =  MutableLiveData()
    }
}