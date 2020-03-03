package com.example.testapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testapp.db.converter.*
import com.example.testapp.db.dao.*
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.CharacterAdvantage
import com.example.testapp.db.entity.CharacterSkills
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.db.entity.advantage.Advantage

@Database(
    entities = [
        Character::class,
        Skill::class,                                                                                                                                            
        Advantage::class,
        CharacterSkills::class,
        CharacterAdvantage::class
    ],
    version = 42,
    exportSchema = false
)
@TypeConverters(
    IntListConverter::class,
    StringListConverter::class,
    SkillDefaultConverter::class,
    PrereqListConverter::class,
    AttributeBonusConverter::class,
    SkillBonusConverter::class,
    ModifierConverter::class
)
abstract class MainDatabase: RoomDatabase()
{
    abstract fun characterDao(): CharacterDao

    abstract fun skillDao(): SkillDao

    abstract fun advantageDao(): AdvantageDao

    abstract fun characterSkillsDao(): CharacterSkillsDao

    abstract fun characterAdvantageDao(): CharacterAdvantageDao
}