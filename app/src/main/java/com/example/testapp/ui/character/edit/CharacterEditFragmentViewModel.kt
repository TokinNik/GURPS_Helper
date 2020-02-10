package com.example.testapp.ui.character.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.CharacterSkills
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import com.example.testapp.util.RollUtil
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class CharacterEditFragmentViewModel(): RxViewModel() {

    private val dbm: DBModelImpl by inject()
    private val rollUtil: RollUtil by inject()

    val getCharacterByIdComplete: LiveData<Character>
        get() = getCharacterByIdEvent

    val error: LiveData<Throwable>
        get() = errorEvent

    val addCharacterComplete: LiveData<Boolean>
        get() = addCharacterCompleteEvent

    val addCharacterSkillsComplete: LiveData<Boolean>
        get() = addCharacterSkillsEvent

    val updateCharacterComplete: LiveData<Boolean>
        get() = updateCharacterCompleteEvent

    val getSkillByNamesComplete: LiveData<List<Skill>>
        get() = getSkillByNamesEvent

    val characterSkillsByIdComplete: LiveData<List<CharacterSkills>>
        get() = characterSkillsByIdEvent

    val getLastCharacterIdComplete: LiveData<Int>
        get() = getLastCharacterIdEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var addCharacterCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var updateCharacterCompleteEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var getCharacterByIdEvent: MutableLiveData<Character> = MutableLiveData()

    private var getSkillByNamesEvent: MutableLiveData<List<Skill>> = MutableLiveData()

    private var addCharacterSkillsEvent: MutableLiveData<Boolean> = MutableLiveData()

    private var characterSkillsByIdEvent: MutableLiveData<List<CharacterSkills>> = MutableLiveData()

    private var getLastCharacterIdEvent: MutableLiveData<Int> = MutableLiveData()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
    }

    fun getCharacterById(id: Int)
    {
        dbm.getDB().characterDao().getById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    getCharacterByIdEvent.value = it
                },
                {
                    errorEvent.value = it
                }
            ).let(compositeDisposable::add)
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

    fun getSkillByNames(characterSkills: List<CharacterSkills>)
    {
        dbm.getDB().skillDao().getByNames(characterSkills.map { it.skillName })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    getSkillByNamesEvent.value = list.filter { skill ->
                        characterSkills.any {
                            it.skillName == skill.name && it.specialization == skill.specialization
                        }
                    }
                },
                {
                    errorEvent.value = it
                }
            ).let(compositeDisposable::add)
    }

    fun getCharacterSkillsById(id: Int) {
        dbm.getDB().characterSkillsDao().getCharacterSkills(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    characterSkillsByIdEvent.value = it
                },
                {
                    errorEvent.value = it
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
        getSkillByNamesEvent =  MutableLiveData()
        getCharacterByIdEvent =  MutableLiveData()
        updateCharacterCompleteEvent =  MutableLiveData()
        addCharacterSkillsEvent =  MutableLiveData()
        characterSkillsByIdEvent = MutableLiveData()
        getLastCharacterIdEvent = MutableLiveData()
    }
}