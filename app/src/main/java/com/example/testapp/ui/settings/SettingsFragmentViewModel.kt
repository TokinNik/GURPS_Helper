package com.example.testapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.db.entity.Character
import com.example.testapp.di.DBModelImpl
import com.example.testapp.ui.RxViewModel
import com.example.testapp.util.DataManager
import com.example.testapp.util.GCSParser
import com.example.testapp.util.SkillsLibLoader
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import java.io.InputStream

class SettingsFragmentViewModel : RxViewModel() {

    private val dataManager: DataManager by inject()

    val error: LiveData<Throwable>
        get() = errorEvent

    private var errorEvent: MutableLiveData<Throwable> = MutableLiveData()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
    }

    fun clearEvents()
    {
        errorEvent =  MutableLiveData()
    }

    fun setColorSchemeValue(scheme: ColorScheme) {
        dataManager.appSettingsVault.colorScheme = scheme.name
    }

    fun getColorScheme(): ColorScheme = ColorScheme.valueOf(dataManager.appSettingsVault.colorScheme)

    fun isNightTheme() = dataManager.appSettingsVault.isNightTheme

    fun setNightTheme(isNightTheme: Boolean) {
        dataManager.appSettingsVault.isNightTheme = isNightTheme
    }
}