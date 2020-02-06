package com.example.testapp.util

import android.content.SharedPreferences
import javax.inject.Inject

class DataManager @Inject constructor(sharedPreferences: SharedPreferences) {

    var appSettingsVault = AppSettingsVault(sharedPreferences)

}