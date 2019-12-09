package com.example.testapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.testapp.db.converter.SkillsConverter

@Entity(tableName = "character")
data class Character(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "name",
    var st: Int = 10,
    var dx: Int = 10,
    var iq: Int = 10,
    var ht: Int = 10,
    var skills: List<Int> = emptyList()
)