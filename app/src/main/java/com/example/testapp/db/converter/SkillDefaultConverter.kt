package com.example.testapp.db.converter

import androidx.room.TypeConverter
import com.example.testapp.db.entity.Skill.Default

class SkillDefaultConverter {
    @TypeConverter
    fun fromList(list: List<Default>): String = list.joinToString(",") { "${it.name}|${it.modifier}|${it.type}|${it.specialization}" }

    @TypeConverter
    fun toList(data: String): List<Default> = data.split(",").map { list ->
        if (!list.isBlank()) {
            var i = 0
            val default = Default()
            list.split("|").forEach {
                when(i){
                    0 -> default.name = it
                    1 -> default.modifier = it
                    2 -> default.type = it
                    3 -> default.specialization = it
                }
                i++
            }
            default
        } else Default()
    }
}