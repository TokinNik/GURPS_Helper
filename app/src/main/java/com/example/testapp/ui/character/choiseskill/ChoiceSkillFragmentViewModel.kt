package com.example.testapp.ui.character.choiseskill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.db.entity.Skill
import com.example.testapp.di.DBModelImpl
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class ChoiceSkillFragmentViewModel: ViewModel() {

    private val dbm: DBModelImpl by inject()

    val error: LiveData<Throwable>
        get() = errorEvent

    val skills: LiveData<List<Skill>>
        get() = skillsEvent

    val skillById: LiveData<Skill>
        get() = skillByIdEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var skillsEvent: MutableLiveData<List<Skill>> = MutableLiveData()

    private var skillByIdEvent: MutableLiveData<Skill> = MutableLiveData()

    private var deleteCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
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

    fun getAllSkills()
    {
        dbm.getDB().skillDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                skillsEvent.value = it
            }.let(compositeDisposable::add)
    }

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        deleteCompleteEvent =  MutableLiveData()
        skillByIdEvent =  MutableLiveData()
        skillsEvent =  MutableLiveData()
    }
}