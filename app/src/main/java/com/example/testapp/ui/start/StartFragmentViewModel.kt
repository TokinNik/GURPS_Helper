package com.example.testapp.ui.start

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


class StartFragmentViewModel: ViewModel() {

    private val dbm: DBModelImpl by inject()

    val characters: LiveData<List<Character>>
        get() = charactersEvent

    private var charactersEvent: MutableLiveData<List<Character>> = MutableLiveData()

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

    fun deleteCharacter(characterId: Int) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.getDB().characterDao().getById(characterId)
                .subscribe(object : DisposableSingleObserver<Character>(){
                override fun onSuccess(t: Character) {
                    dbm.db.characterDao().delete(t)
                }

                override fun onError(e: Throwable) {
                    //err
                }
            })
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