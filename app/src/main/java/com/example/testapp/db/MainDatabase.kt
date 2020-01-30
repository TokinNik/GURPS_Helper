package com.example.testapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testapp.db.converter.SkillsConverter
import com.example.testapp.db.dao.CharacterDao
import com.example.testapp.db.dao.SkillDao
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill

@Database(
    entities = [
        Character::class,
        Skill::class
    ],
    version = 7,
    exportSchema = false
)
@TypeConverters(SkillsConverter::class)
abstract class MainDatabase: RoomDatabase()
{
    abstract fun characterDao(): CharacterDao

    abstract fun skillDao(): SkillDao
}