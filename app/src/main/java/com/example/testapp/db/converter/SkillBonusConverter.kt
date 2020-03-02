package com.example.testapp.db.converter

import androidx.room.TypeConverter
import com.example.testapp.db.entity.advantage.SkillBonus

class SkillBonusConverter {
    @TypeConverter
    fun fromList(list: List<SkillBonus>): String = list.joinToString(",") { "${it.name}|${it.nameCompare}|${it.specialization}|${it.specializationCompare}|${it.amount}" }

    @TypeConverter
    fun toList(data: String): List<SkillBonus> = data.split(",").map { list ->
        if (!list.isBlank()) {
            var i = 0
            val default = SkillBonus()
            list.split("|").forEach {
                when(i){
                    0 -> default.name = it
                    1 -> default.nameCompare = it
                    2 -> default.specialization = it
                    3 -> default.specializationCompare = it
                    4 -> default.amount = it.toInt()
                }
                i++
            }
            default
        } else SkillBonus()
    }
}