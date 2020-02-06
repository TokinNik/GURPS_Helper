package com.example.testapp.util.preference_delegate

import android.content.SharedPreferences

class BooleanPreferenceDelegate(private val defValue: Boolean, name: String? = null) : PreferenceDelegate<Boolean>(name) {

    override fun getValue(prefs: SharedPreferences, key: String) = prefs.getBoolean(key, defValue)

    override fun setValue(prefs: SharedPreferences, key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }
}