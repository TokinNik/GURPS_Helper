package com.example.testapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testapp.db.converter.*
import com.example.testapp.db.dao.CharacterDao
import com.example.testapp.db.dao.CharacterSkillsDao
import com.example.testapp.db.dao.SkillDao
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.CharacterSkills
import com.example.testapp.db.entity.Skill.Skill

@Database(
    entities = [
        Character::class,
        Skill::class,
        CharacterSkills::class
    ],
    version = 30,
    exportSchema = false
)
@TypeConverters(IntListConverter::class, StringListConverter::class, SkillDefaultConverter::class, PrereqListConverter::class)
abstract class MainDatabase: RoomDatabase()
{
    abstract fun characterDao(): CharacterDao

    abstract fun skillDao(): SkillDao

    abstract fun characterSkillsDao(): CharacterSkillsDao


}