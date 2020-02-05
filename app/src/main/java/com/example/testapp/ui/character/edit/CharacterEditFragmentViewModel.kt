package com.example.testapp.ui.character.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.di.DBModelImpl
import com.example.testapp.util.RollUtil
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

    private val rollUtil: RollUtil by inject()

    val characterById: LiveData<Character>
        get() = characterByIdEvent

    val error: LiveData<Throwable>
        get() = errorEvent

    val addComplete: LiveData<Boolean>
        get() = addCompleteEvent

    val updateComplete: LiveData<Boolean>
        get() = updateCompleteEvent

    val skillByIds: LiveData<List<Skill>>
        get() = skillByIdsEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var addCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var updateCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var characterByIdEvent: MutableLiveData<Character> = MutableLiveData()

    private var skillByIdsEvent: MutableLiveData<List<Skill>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
        println(rollUtil.roll3D6())
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
                updateCompleteEvent.value = true
            }
            .subscribe()
    }

    fun getSkillByIds(id: List<Int>)
    {
        dbm.getDB().skillDao().getByIds(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<List<Skill>>(){
                override fun onSuccess(t: List<Skill>) {
                    skillByIdsEvent.value = t
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
                skillByIdsEvent.value = it
            }.let(compositeDisposable::add)
    }

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        addCompleteEvent =  MutableLiveData()
        skillByIdsEvent =  MutableLiveData()
        characterByIdEvent =  MutableLiveData()
        updateCompleteEvent =  MutableLiveData()
    }
}