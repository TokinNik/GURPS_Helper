package com.example.testapp.db.converter

import androidx.room.TypeConverter
import com.example.testapp.db.entity.advantage.Modifier

class ModifierConverter {
    @TypeConverter
    fun fromList(list: List<Modifier>): String = list.joinToString(",") { "${it.version}|${it.enabled}|${it.name}|${it.costType}|${it.cost}|${it.affects}|${it.reference}" }

    @TypeConverter
    fun toList(data: String): List<Modifier> = data.split(",").map { list ->
        if (!list.isBlank()) {
            var i = 0
            val default = Modifier()
            list.split("|").forEach {
                when(i){
                    0 -> default.version = it
                    1 -> default.enabled = it == "yes"
                    2 -> default.name = it
                    3 -> default.costType = it
                    4 -> default.cost = it.toInt()
                    5 -> default.affects = it
                    6 -> default.reference = it
                }
                i++
            }
            default
        } else Modifier()
    }
}