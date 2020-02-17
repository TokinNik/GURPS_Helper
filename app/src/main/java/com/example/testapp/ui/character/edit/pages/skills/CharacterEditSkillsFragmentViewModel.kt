package com.example.testapp.ui.character.edit.pages.skills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.CharacterSkills
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import com.example.testapp.ui.settings.ColorScheme
import com.example.testapp.util.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class CharacterEditSkillsFragmentViewModel : RxViewModel() {

    private val dbm: DBModelImpl by inject()
    private val dataManager: DataManager by inject()

    val error: LiveData<Throwable>
        get() = errorEvent

    val getSkillByNamesComplete: LiveData<List<Skill>>
        get() = getSkillByNamesEvent

    val characterSkillsByIdComplete: LiveData<List<CharacterSkills>>
        get() = characterSkillsByIdEvent

    val colorScheme: LiveData<ColorScheme>
        get() = colorSchemeEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var getSkillByNamesEvent: MutableLiveData<List<Skill>> = MutableLiveData()

    private var characterSkillsByIdEvent: MutableLiveData<List<CharacterSkills>> = MutableLiveData()

    private var colorSchemeEvent: MutableLiveData<ColorScheme> = MutableLiveData()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
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


    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        getSkillByNamesEvent =  MutableLiveData()
        characterSkillsByIdEvent = MutableLiveData()
        colorSchemeEvent = MutableLiveData()
    }

    fun getEditCharacter(): Character = dataManager.runtimeCharacterEdit.value ?: Character()

    fun setEditSkills(skillList: List<Skill>) {
        dataManager.runtimeCharacterSkillsEdit.value = skillList
    }

    fun getColorScheme() {
        colorSchemeEvent.value = ColorScheme.valueOf(dataManager.appSettingsVault.colorScheme)
    }
}