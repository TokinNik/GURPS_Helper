package com.example.testapp.di.modules

import android.app.Application
import android.content.Context
import com.example.testapp.util.*
import toothpick.smoothie.module.SmoothieApplicationModule

class AppModule(application: Application) : SmoothieApplicationModule(application) {

    private val SETTINGS = "settings"

    init {
        bind(Application::class.java).toInstance(application)
        bind(Context::class.java).toInstance(application.applicationContext)
        bind(RollUtil::class.java).to(RollUtil::class.java)
        bind(GCSParser::class.java).to(GCSParser::class.java)
        bind(SkillsLibLoader::class.java).to(SkillsLibLoader::class.java)
        bind(GurpsCalculations::class.java).to(GurpsCalculations::class.java)
        bind(DataManager::class.java).toInstance(DataManager(application.applicationContext.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)))
    }
}