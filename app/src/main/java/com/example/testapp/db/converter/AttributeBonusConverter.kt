package com.example.testapp.db.converter

import androidx.room.TypeConverter
import com.example.testapp.db.entity.advantage.AttributeBonus
import com.example.testapp.db.entity.advantage.SkillBonus

class AttributeBonusConverter {
    @TypeConverter
    fun fromList(list: List<AttributeBonus>): String = list.joinToString(",") { "${it.attribute}|${it.amount}" }

    @TypeConverter
    fun toList(data: String): List<AttributeBonus> = data.split(",").map { list ->
        if (!list.isBlank()) {
            var i = 0
            val default = AttributeBonus()
            list.split("|").forEach {
                when(i){
                    0 -> default.attribute= it
                    1 -> default.amount = it.toInt()
                }
                i++
            }
            default
        } else AttributeBonus()
    }
}