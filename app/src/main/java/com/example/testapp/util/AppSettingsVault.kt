package com.example.testapp.util

import android.content.SharedPreferences
import com.example.mts_pass_refactor.utils.preference_delegate.SharedPreferencesHolder

class AppSettingsVault(override val sharedPreferences: SharedPreferences) : SharedPreferencesHolder {
    var isSkilLibLoaded: Boolean by booleanDelegate(false, "is_skill_lib_loaded")

    fun clearSettings(){
        isSkilLibLoaded = false
    }
}