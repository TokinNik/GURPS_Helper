package com.example.mts_pass_refactor.utils.preference_delegate

import android.content.SharedPreferences
import com.example.testapp.util.preference_delegate.BooleanPreferenceDelegate
import com.example.testapp.util.preference_delegate.FloatPreferenceDelegate
import com.example.testapp.util.preference_delegate.IntPreferenceDelegate
import com.example.testapp.util.preference_delegate.StringPreferenceDelegate

interface SharedPreferencesHolder {
    val sharedPreferences: SharedPreferences

    fun booleanDelegate(defValue: Boolean = false, name: String? = null) =
        BooleanPreferenceDelegate(defValue, name)

    fun intDelegate(defValue: Int = 0, name: String? = null) =
        IntPreferenceDelegate(defValue, name)

    fun floatDelegate(defValue: Float = 0f, name: String? = null) =
        FloatPreferenceDelegate(defValue, name)

    fun stringDelegate(defValue: String = "", name: String? = null) =
        StringPreferenceDelegate(defValue, name)
}