package com.example.testapp.db.entity

import androidx.room.*

@Entity(
    tableName = "Character_skills",
    foreignKeys = [
    ForeignKey(entity = Character::class, parentColumns = ["id"], childColumns = ["character_id"])
    //ForeignKey(entity = Skill::class, parentColumns = ["name"], childColumns = ["skill_name"])
    ],
    indices = [
        Index(value = ["character_id", "skill_name"])
    ])
data class CharacterSkills(
    @ColumnInfo(name = "character_id")
    var characterId: Int = 0,
    @ColumnInfo(name = "skill_name")
    @PrimaryKey
    var skillName: String = "",
    var container: String = "empty",
    var points: String = ""
)