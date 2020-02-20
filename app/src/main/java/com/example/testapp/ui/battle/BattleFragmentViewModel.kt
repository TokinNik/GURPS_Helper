package com.example.testapp.ui.battle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.CharacterSkills
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import com.example.testapp.ui.settings.ColorScheme
import com.example.testapp.util.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class BattleFragmentViewModel : RxViewModel() {

    private val dbm: DBModelImpl by inject()
    private val dataManager:DataManager by inject()

    val characters: LiveData<List<Character>>
        get() = charactersEvent

    val error: LiveData<Throwable>
        get() = errorEvent

    val colorScheme: LiveData<ColorScheme>
        get() = colorSchemeEvent

    val characterSkillsByIdComplete: LiveData<List<CharacterSkills>>
        get() = characterSkillsByIdEvent

    val getSkillByNamesComplete: LiveData<List<Skill>>
        get() = getSkillByNamesEvent

    private var charactersEvent: MutableLiveData<List<Character>> = MutableLiveData()

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var colorSchemeEvent: MutableLiveData<ColorScheme> = MutableLiveData()

    private var characterSkillsByIdEvent: MutableLiveData<List<CharacterSkills>> = MutableLiveData()

    private var getSkillByNamesEvent: MutableLiveData<List<Skill>> = MutableLiveData()

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
                    getSkillByNamesEvent.value
                },
                {
                    errorEvent.value = it
                }
            ).let(compositeDisposable::add)
    }

    fun getColorScheme() {
        colorSchemeEvent.value = ColorScheme.valueOf(dataManager.appSettingsVault.colorScheme)
    }

    fun clearEvents()
    {
        charactersEvent = MutableLiveData()
        errorEvent = MutableLiveData()
        colorSchemeEvent = MutableLiveData()
        characterSkillsByIdEvent = MutableLiveData()
        getSkillByNamesEvent = MutableLiveData()
    }
}