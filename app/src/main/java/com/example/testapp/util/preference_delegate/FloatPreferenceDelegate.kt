package com.example.testapp.util.preference_delegate

import android.content.SharedPreferences

class FloatPreferenceDelegate(private val defValue: Float, name: String? = null) : PreferenceDelegate<Float>(name) {

    override fun getValue(prefs: SharedPreferences, key: String) = prefs.getFloat(key, defValue)

    override fun setValue(prefs: SharedPreferences, key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }
}