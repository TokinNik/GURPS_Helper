package com.example.testapp.di

import android.app.Application
import androidx.room.Room
import com.example.testapp.db.MainDatabase
import com.example.testapp.db.entity.Character
import javax.inject.Inject

class DBModelImpl @Inject constructor(private val application: Application) : DBModel {

    val db =  Room.databaseBuilder<MainDatabase>(
    application,
    MainDatabase::class.java,
    "database")
//       .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    override fun getDB(): MainDatabase = db
}