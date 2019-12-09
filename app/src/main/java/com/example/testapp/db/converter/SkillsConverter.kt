package com.example.testapp.db.converter

import androidx.room.TypeConverter

class SkillsConverter {

    @TypeConverter
    fun fromSkills(skills: List<Int>): String =skills.joinToString(",")

    @TypeConverter
    fun toSkills(data: String): List<Int> =data.split(",").map { if (!it.isBlank()) it.toInt() else 0 }

}