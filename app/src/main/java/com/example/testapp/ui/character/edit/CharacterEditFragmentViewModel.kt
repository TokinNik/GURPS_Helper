package com.example.testapp.ui.character.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import com.example.testapp.util.DataManager
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class CharacterEditFragmentViewModel(): RxViewModel() {

    private val dbm: DBModelImpl by inject()
    private val dataManager: DataManager by inject()

    val error: LiveData<Throwable>
        get() = errorEvent

    val addCharacterComplete: LiveData<Boolean>
        get() = addCharacterCompleteEvent

    val addCharacterSkillsComplete: LiveData<Boolean>
        get() = addCharacterSkillsEvent

    val updateCharacterComplete: LiveData<Boolean>
        get() = updateCharacterCompleteEvent

    val getLastCharacterIdComplete: LiveData<Int>
        get() = getLastCharacterIdEvent

    val changeEditCharacter: LiveData<Character>
        get() = changeEditCharacterEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var addCharacterCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var updateCharacterCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var addCharacterSkillsEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var getLastCharacterIdEvent: MutableLiveData<Int> = MutableLiveData()

    private var changeEditCharacterEvent: MutableLiveData<Character> = MutableLiveData()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
    }

    fun addCharacter(character: Character){
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.characterDao().insert(character)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                {
                    errorEvent.value = it
                },
                {
                    addCharacterCompleteEvent.value = true
                }
            ).let(compositeDisposable::add)
    }

    fun updateCharacter(character: Character) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.db.characterDao().update(character)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                {
                    errorEvent.value = it
                },
                {
                    updateCharacterCompleteEvent.value = true
                }
            ).let(compositeDisposable::add)
    }

    fun addCharacterSkills(characterSkillList: List<Skill>, characterId: Int) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            dbm.saveCharacterSkills(characterSkillList, characterId)
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                {
                    errorEvent.value = it
                },
                {
                    addCharacterSkillsEvent.value = true
                }
            ).let(compositeDisposable::add)
    }

    fun getLastCharacterId() {
        dbm.getDB().characterDao().getLastCharacterId()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    getLastCharacterIdEvent.value = it
                },
                {
                    errorEvent.value = it
                }
            ).let(compositeDisposable::add)
    }


    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        addCharacterCompleteEvent =  MutableLiveData()
        updateCharacterCompleteEvent =  MutableLiveData()
        addCharacterSkillsEvent =  MutableLiveData()
        getLastCharacterIdEvent = MutableLiveData()
        changeEditCharacterEvent = MutableLiveData()
    }

    fun setEditCharacterId(id: Int) {
     dataManager.runtimeCharacterEdit.value?.id = id
    }

    fun getEditCharacter(): MutableLiveData<Character> = dataManager.runtimeCharacterEdit

    fun getEditCharacterSkills(): MutableLiveData<List<Skill>> = dataManager.runtimeCharacterSkillsEdit

    fun clearEditCharacter(){
        dataManager.runtimeCharacterEdit.value = Character()
        dataManager.runtimeCharacterSkillsEdit.value = emptyList()
    }
}