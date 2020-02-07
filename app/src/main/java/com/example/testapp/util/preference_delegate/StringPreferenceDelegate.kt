package com.example.testapp.util.preference_delegate

import android.content.SharedPreferences

class StringPreferenceDelegate(private val defValue: String, name: String? = null) : PreferenceDelegate<String>(name) {

    override fun getValue(prefs: SharedPreferences, key: String) = prefs.getString(key, defValue) ?: defValue

    override fun setValue(prefs: SharedPreferences, key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}