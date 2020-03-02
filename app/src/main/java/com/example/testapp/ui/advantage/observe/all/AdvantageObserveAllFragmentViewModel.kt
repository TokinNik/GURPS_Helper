package com.example.testapp.ui.advantage.observe.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.db.entity.advantage.Advantage
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class AdvantageObserveAllFragmentViewModel : RxViewModel() {

    private val dbm: DBModelImpl by inject()

    val error: LiveData<Throwable>
        get() = errorEvent

    val getAllAdvantage: LiveData<List<Advantage>>
        get() = skillsEvent

    val searchAdvantagesComplete: LiveData<List<Advantage>>
        get() = searchAdvantagesEvent

    val skillById: LiveData<Advantage>
        get() = skillByIdEvent

    val deleteComplete: LiveData<Boolean>
        get() = deleteCompleteEvent

    val searchAdvantageComplete: LiveData<Advantage>
        get() = searchAdvantageEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var skillsEvent: MutableLiveData<List<Advantage>> = MutableLiveData()

    private var skillByIdEvent: MutableLiveData<Advantage> = MutableLiveData()

    private var deleteCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var searchAdvantageEvent: MutableLiveData<Advantage> = MutableLiveData()

    private var searchAdvantagesEvent: MutableLiveData<List<Advantage>> = MutableLiveData()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
    }

    fun getAdvantageById(id: Int) {
        dbm.getDB().advantageDao().getById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    skillByIdEvent.value = it
                },
                {
                    errorEvent.value = it
                }).let(compositeDisposable::add)
    }

    fun getAllAdvantages()
    {
        dbm.getDB().advantageDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                skillsEvent.value = it
            }.let(compositeDisposable::add)
    }

    fun deleteAdvantage(currentAdvantage: Advantage) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.advantageDao().delete(currentAdvantage)
            emitter.onComplete()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                {
                    errorEvent.value = it
                },
                {
                    deleteCompleteEvent.value = true
                }).let(compositeDisposable::add)
    }

    fun searchAdvantage(query: String) {
        dbm.getDB().advantageDao().searchAdvantage("%$query%")//part of
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    searchAdvantageEvent.value = it
                },
                {
                    errorEvent.value = it
                }).let(compositeDisposable::add)
    }

    fun searchAdvantages(query: String) {
        dbm.getDB().advantageDao().searchAdvantages("$query%")//start with
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    searchAdvantagesEvent.value = it
                },
                {
                    errorEvent.value = it
                }).let(compositeDisposable::add)
    }

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        deleteCompleteEvent =  MutableLiveData()
        skillByIdEvent =  MutableLiveData()
        skillsEvent =  MutableLiveData()
        searchAdvantageEvent =  MutableLiveData()
        searchAdvantagesEvent =  MutableLiveData()
    }
}