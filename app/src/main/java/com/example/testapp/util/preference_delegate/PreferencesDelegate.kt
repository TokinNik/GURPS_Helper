package com.example.testapp.util.preference_delegate

import android.content.SharedPreferences
import com.example.mts_pass_refactor.utils.preference_delegate.SharedPreferencesHolder
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class PreferenceDelegate<T>(val name: String? = null) : ReadWriteProperty<SharedPreferencesHolder, T> {

    private fun getPreferenceKey(property: KProperty<*>) = name ?: property.name

    final override fun getValue(thisRef: SharedPreferencesHolder, property: KProperty<*>): T {
        return getValue(thisRef.sharedPreferences, getPreferenceKey(property))
    }

    final override fun setValue(thisRef: SharedPreferencesHolder, property: KProperty<*>, value: T) {
        setValue(thisRef.sharedPreferences, getPreferenceKey(property), value)
    }

    abstract fun getValue(prefs: SharedPreferences, key: String): T

    abstract fun setValue(prefs: SharedPreferences, key: String, value: T)
}
