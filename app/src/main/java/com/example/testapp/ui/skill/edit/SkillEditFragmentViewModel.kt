package com.example.testapp.ui.skill.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill
import com.example.testapp.di.DBModelImpl
import com.example.testapp.util.RollUtil
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class SkillEditFragmentViewModel(): ViewModel() {

    private val dbm: DBModelImpl by inject()

    private val rollUtil: RollUtil by inject()

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
        println(rollUtil.roll3D6())
    }

    fun getSkillById(id: Int) {
        dbm.getDB().skillDao().getById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<Skill>() {
                override fun onSuccess(t: Skill) {
                    skillByIdEvent.value = t
                }

                override fun onError(e: Throwable) {
                    errorEvent.value = e
                }
            })
    }

    fun addSkill(skill: Skill) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.skillDao().insert(skill)
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

    fun updateSkill(skill: Skill) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.skillDao().update(skill)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                errorEvent.value = it
            }
            .doOnComplete {
                updateCompleteEvent.value = true
            }
            .subscribe()
    }

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        addCompleteEvent =  MutableLiveData()
        updateCompleteEvent =  MutableLiveData()
        skillByIdEvent =  MutableLiveData()
    }
}