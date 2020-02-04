package com.example.testapp.db.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "Character_skills",
    foreignKeys = [
    ForeignKey(entity = Character::class, parentColumns = ["id"], childColumns = ["character_id"], onDelete = CASCADE)
    ],
    primaryKeys = ["character_id", "skill_name"])
data class CharacterSkills(
    @ColumnInfo(name = "character_id")
    var characterId: Int = 0,
    @ColumnInfo(name = "skill_name")
    var skillName: String = "",
    var container: String = "empty",
    var points: String = ""
)