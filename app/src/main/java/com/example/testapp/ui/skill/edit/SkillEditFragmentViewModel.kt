package com.example.testapp.ui.skill.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import com.example.testapp.util.RollUtil
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class SkillEditFragmentViewModel : RxViewModel() {

    private val dbm: DBModelImpl by inject()

    val skillById: LiveData<Skill>
        get() = skillByIdEvent

    val error: LiveData<Throwable>
        get() = errorEvent

    val addComplete: LiveData<Boolean>
        get() = addCompleteEvent

    val updateComplete: LiveData<Boolean>
        get() = updateCompleteEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var addCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var updateCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var skillByIdEvent: MutableLiveData<Skill> = MutableLiveData()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
    }

    fun getSkillById(id: Int) {
        dbm.getDB().skillDao().getById(id)
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

    fun addSkill(skill: Skill) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.skillDao().insert(skill)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                {
                    errorEvent.value = it
                },
                {
                    addCompleteEvent.value = true
                }
            ).let(compositeDisposable::add)
    }

    fun updateSkill(skill: Skill) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.skillDao().update(skill)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                {
                    errorEvent.value = it
                },
                {
                    updateCompleteEvent.value = true
                }
            ).let(compositeDisposable::add)
    }

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        addCompleteEvent =  MutableLiveData()
        updateCompleteEvent =  MutableLiveData()
        skillByIdEvent =  MutableLiveData()
    }
}