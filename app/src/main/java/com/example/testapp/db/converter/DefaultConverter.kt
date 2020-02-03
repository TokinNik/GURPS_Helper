package com.example.testapp.db.converter

import androidx.room.TypeConverter
import com.example.testapp.db.entity.Default

class DefaultConverter {
    @TypeConverter
    fun fromList(list: List<Default>): String = list.joinToString(",") { "${it.name}|${it.modifier}|${it.type}" }

    @TypeConverter
    fun toList(data: String): List<Default> = data.split(",").map {
        if (!it.isBlank()) {
            var i = 0
            val default = Default()
            it.split("|").forEach {
                when(i){
                    0 -> default.name = it
                    1 -> default.name = it
                    2 -> default.name = it
                }
                i++
            }
            default
        } else Default()
    }
}