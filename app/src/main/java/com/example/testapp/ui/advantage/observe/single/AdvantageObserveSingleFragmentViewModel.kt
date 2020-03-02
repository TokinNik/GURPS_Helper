package com.example.testapp.ui.advantage.observe.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.db.entity.advantage.Advantage
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class AdvantageObserveSingleFragmentViewModel : RxViewModel() {

    private val dbm: DBModelImpl by inject()

    val error: LiveData<Throwable>
        get() = errorEvent

    val getAllAdvantagesComplete: LiveData<List<Advantage>>
        get() = skillsEvent

    val getAdvantageByIdComplete: LiveData<Advantage>
        get() = skillByIdEvent

    val deleteComplete: LiveData<Boolean>
        get() = deleteCompleteEvent

    val getAdvantageByNameComplete: LiveData<Advantage>
        get() = getAdvantageByNameEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var skillsEvent: MutableLiveData<List<Advantage>> = MutableLiveData()

    private var skillByIdEvent: MutableLiveData<Advantage> = MutableLiveData()

    private var deleteCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var getAdvantageByNameEvent: MutableLiveData<Advantage> = MutableLiveData()

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
                }
            ).let(compositeDisposable::add)
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
            .doOnError {
                errorEvent.value = it
            }
            .doOnComplete {
                deleteCompleteEvent.value = true
            }
            .subscribe()
            .let(compositeDisposable::add)
    }

    fun getAdvantageByName(name: String) {
        dbm.getDB().advantageDao().getByName(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    getAdvantageByNameEvent.value = it
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
        skillByIdEvent =  MutableLiveData()
        skillsEvent =  MutableLiveData()
    }
}