package com.example.testapp.util

import android.content.SharedPreferences
import com.example.mts_pass_refactor.utils.preference_delegate.SharedPreferencesHolder
import com.example.testapp.ui.settings.ColorScheme

class AppSettingsVault(override val sharedPreferences: SharedPreferences) : SharedPreferencesHolder {

    var isSkilLibLoaded: Boolean by booleanDelegate(false, "is_skill_lib_loaded")
    var isNightTheme: Boolean by booleanDelegate(false , "is_night_theme")
    var colorScheme: String by stringDelegate(ColorScheme.CLASSIC.name , "color_scheme")
    var fileIntentAdd: String by stringDelegate("" , "file_intent_add")


    fun clearSettings(){
        isSkilLibLoaded = false
    }
}