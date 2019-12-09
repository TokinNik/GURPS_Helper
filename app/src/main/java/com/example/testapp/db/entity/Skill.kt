package com.example.testapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skill")
data class Skill(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    var name: String = "skill_name",
    var mainAttribute: String = "none",
    var level: Int = 0
)