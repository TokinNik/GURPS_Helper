package com.example.testapp.db

import androidx.room.ColumnInfo

data class SkillsAndCharacterOnSt(
    val id: Int,
    val name: String,
    @ColumnInfo(name = "skill_id")val skillId: Int,
    @ColumnInfo(name = "skill_name") val skillName: String
)