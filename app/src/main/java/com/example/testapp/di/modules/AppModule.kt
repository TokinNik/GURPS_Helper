package com.example.testapp.di.modules

import android.app.Application
import android.content.Context
import com.example.testapp.util.RollUtil
import toothpick.smoothie.module.SmoothieApplicationModule

class AppModule(application: Application) : SmoothieApplicationModule(application) {

    init {
        bind(Application::class.java).toInstance(application)
        bind(Context::class.java).toInstance(application.applicationContext)
        bind(RollUtil::class.java).to(RollUtil::class.java)
    }
}