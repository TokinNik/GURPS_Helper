package com.example.testapp.di.modules

import com.example.testapp.di.DBModel
import com.example.testapp.di.DBModelImpl
import toothpick.config.Module

class DBModule() : Module() {
    init {
        bind(DBModel::class.java)
            .to(DBModelImpl::class.java)
            .singleton()

    }
}