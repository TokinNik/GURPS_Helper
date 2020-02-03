package com.example.testapp.db.converter

import androidx.room.TypeConverter

class CategoriesConverter {
    @TypeConverter
    fun fromCategories(categories: List<String>): String = categories.joinToString(",")

    @TypeConverter
    fun toCategories(data: String): List<String> = data.split(",").map { if (!it.isBlank()) it else "" }
}