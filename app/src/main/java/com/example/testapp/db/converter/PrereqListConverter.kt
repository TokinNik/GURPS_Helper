package com.example.testapp.db.converter

import androidx.room.TypeConverter
import com.example.testapp.db.entity.Skill.PrereqList

class PrereqListConverter {

    private val skillConverter = SkillPrereqConverter()

    @TypeConverter
    fun fromList(list: List<PrereqList>): String = list.joinToString(",") { "${it.all}+${it.depth}+${it.parent}+${skillConverter.fromList(it.skillPrereqList)}"}

    @TypeConverter
    fun toList(data: String): List<PrereqList> = data.split(",").map { list ->
        if (!list.isBlank()) {
            var i = 0
            val prereqList = PrereqList()
            list.split("+").forEach {
                when (i) {
                    0 -> prereqList.all = it == "yes"
                    1 -> prereqList.depth = it.toInt()
                    2 -> prereqList.parent = it.toInt()
                    3 -> {
                        prereqList.skillPrereqList = skillConverter.toList(it)
                        return@forEach
                    }
                }
                i++
            }
            prereqList
        } else PrereqList()
    }
}