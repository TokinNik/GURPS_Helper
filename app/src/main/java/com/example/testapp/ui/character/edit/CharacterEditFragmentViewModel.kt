package com.example.testapp.ui.character.edit

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


class CharacterEditFragmentViewModel(): ViewModel() {

    private val dbm: DBModelImpl by inject()

    val characterById: LiveData<Character>
        get() = characterByIdEvent

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
                    //err
                }
            })
    }

    fun addCharacter(character: Character){
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.characterDao().insert(character)
            emitter.onNext(1)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                //Error action
            }
            .doOnComplete {
                //Complete action
            }
            .subscribe()
    }

    fun updateCharacter(character: Character) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.characterDao().update(character)
            emitter.onNext(1)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                //Error action
            }
            .doOnComplete {
                //Complete action
            }
            .subscribe()
    }
}