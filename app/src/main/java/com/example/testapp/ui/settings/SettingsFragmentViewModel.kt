package com.example.testapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.ui.RxViewModel
import com.example.testapp.util.DataManager
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

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