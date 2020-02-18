package com.example.testapp.db.converter

import androidx.room.TypeConverter
import com.example.testapp.db.entity.Skill.SkillPrereq

class SkillPrereqConverter {

    @TypeConverter
    fun fromList(list: List<SkillPrereq>): String = list.joinToString("*") { "${it.has}|${it.nameCompare}|${it.name}|${it.specializationCompare}|${it.specialization}|${it.levelCompare}|${it.level}"}

    @TypeConverter
    fun toList(data: String): List<SkillPrereq> = data.split("*").map { list ->
        if (!list.isBlank()) {
            var i = 0
            val skillPrereq = SkillPrereq()
            list.split("|").forEach {
                when (i) {
                    0 -> skillPrereq.has = it == "yes"
                    1 -> skillPrereq.nameCompare = it
                    2 -> skillPrereq.name = it
                    3 -> skillPrereq.specializationCompare = it
                    4 -> skillPrereq.specialization = it
                    5 -> skillPrereq.levelCompare = it
                    6 -> skillPrereq.level = it
                }
                i++
            }
            skillPrereq
        } else SkillPrereq()
    }

}