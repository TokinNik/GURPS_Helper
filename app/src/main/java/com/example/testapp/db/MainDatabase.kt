package com.example.testapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testapp.db.dao.CharacterDao
import com.example.testapp.db.entity.Character

@Database(
    entities = [Character::class],
    version = 3,
    exportSchema = false
)
abstract class MainDatabase: RoomDatabase()
{
    abstract fun characterDao(): CharacterDao
}