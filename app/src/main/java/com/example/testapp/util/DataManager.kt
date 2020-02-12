package com.example.testapp.util

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill.Skill
import javax.inject.Inject

class DataManager @Inject constructor(sharedPreferences: SharedPreferences) {

    var appSettingsVault = AppSettingsVault(sharedPreferences)

    var runtimeCharacterEdit: MutableLiveData<Character> = MutableLiveData(Character())

    var runtimeCharacterSkillsEdit: MutableLiveData<List<Skill>> = MutableLiveData(emptyList())

}