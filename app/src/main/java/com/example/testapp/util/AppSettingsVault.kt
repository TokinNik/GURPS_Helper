package com.example.testapp.util

import android.content.SharedPreferences
import com.example.mts_pass_refactor.utils.preference_delegate.SharedPreferencesHolder
import com.example.testapp.ui.settings.ColorScheme

class AppSettingsVault(override val sharedPreferences: SharedPreferences) : SharedPreferencesHolder {

    var isSkilLibLoaded: Boolean by booleanDelegate(false, "is_skill_lib_loaded")
    var colorScheme: String by stringDelegate(ColorScheme.CLASSIC.name , "color_scheme")

    fun clearSettings(){
        isSkilLibLoaded = false
    }
}