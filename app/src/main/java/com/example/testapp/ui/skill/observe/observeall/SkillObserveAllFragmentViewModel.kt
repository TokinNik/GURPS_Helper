package com.example.testapp.ui.skill.observe.observeall

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.db.entity.Skill.Skill
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


class SkillObserveAllFragmentViewModel : RxViewModel() {

    private val dbm: DBModelImpl by inject()

    val error: LiveData<Throwable>
        get() = errorEvent

    val skills: LiveData<List<Skill>>
        get() = skillsEvent

    val searchSkillsComplete: LiveData<List<Skill>>
        get() = searchSkillsEvent

    val skillById: LiveData<Skill>
        get() = skillByIdEvent

    val deleteComplete: LiveData<Boolean>
        get() = deleteCompleteEvent

    val searchSkillComplete: LiveData<Skill>
        get() = searchSkillEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var skillsEvent: MutableLiveData<List<Skill>> = MutableLiveData()

    private var skillByIdEvent: MutableLiveData<Skill> = MutableLiveData()

    private var deleteCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var searchSkillEvent: MutableLiveData<Skill> = MutableLiveData()

    private var searchSkillsEvent: MutableLiveData<List<Skill>> = MutableLiveData()

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
                }).let(compositeDisposable::add)
    }

    fun getAllSkills()
    {
        dbm.getDB().skillDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                skillsEvent.value = it
            }.let(compositeDisposable::add)
    }

    fun deleteSkill(currentSkill: Skill) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.skillDao().delete(currentSkill)
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

    fun searchSkill(query: String) {
        dbm.getDB().skillDao().searchSkill("%$query%")//part of
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    searchSkillEvent.value = it
                },
                {
                    errorEvent.value = it
                }).let(compositeDisposable::add)
    }

    fun searchSkills(query: String) {
        dbm.getDB().skillDao().searchSkills("$query%")//start with
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    searchSkillsEvent.value = it
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
        searchSkillEvent =  MutableLiveData()
        searchSkillsEvent =  MutableLiveData()
    }
}