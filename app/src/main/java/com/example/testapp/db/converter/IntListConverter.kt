package com.example.testapp.db.converter

import androidx.room.TypeConverter

class IntListConverter {

    @TypeConverter
    fun fromList(list: List<Int>): String = list.joinToString(",")

    @TypeConverter
    fun toList(data: String): List<Int> = data.split(",").map { if (!it.isBlank()) it.toInt() else 0 }

}