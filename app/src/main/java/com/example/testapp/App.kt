package com.example.testapp

import android.app.Application
import com.example.testapp.di.modules.AppModule
import com.example.testapp.di.modules.DBModule
import com.example.testapp.di.modules.UIModule
import toothpick.Toothpick

class App: Application()
{

    override fun onCreate() {
        super.onCreate()
        val appScope = Toothpick.openScope("APP")
        appScope.installModules(AppModule(this))
        appScope.installModules(DBModule())
        appScope.installModules(UIModule())
        appScope.inject(this)

    }
}