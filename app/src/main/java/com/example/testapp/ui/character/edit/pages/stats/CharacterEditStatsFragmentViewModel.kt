package com.example.testapp.ui.character.edit.pages.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.db.entity.Character
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import com.example.testapp.util.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject


class CharacterEditStatsFragmentViewModel : RxViewModel() {

    private val dbm: DBModelImpl by inject()
    private val dataManager: DataManager by inject()

    val getCharacterByIdComplete: LiveData<Character>
        get() = getCharacterByIdEvent

    val error: LiveData<Throwable>
        get() = errorEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    private var getCharacterByIdEvent: MutableLiveData<Character> = MutableLiveData()

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

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
        getCharacterByIdEvent =  MutableLiveData()
    }

    fun getEditCharacter(): Character = dataManager.runtimeCharacterEdit.value ?: Character()

    fun setEditCharacter(character: Character) {
        dataManager.runtimeCharacterEdit.value = character
    }
}