package com.example.testapp.util.preference_delegate

import android.content.SharedPreferences

class IntPreferenceDelegate(private val defValue: Int, name: String? = null) : PreferenceDelegate<Int>(name) {

    override fun getValue(prefs: SharedPreferences, key: String) = prefs.getInt(key, defValue)

    override fun setValue(prefs: SharedPreferences, key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }
}