package com.example.testapp.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "skill")
data class Skill(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @Ignore//todo ???
    var container: String = "empty",
    var name: String = "",
    var name_loc: String = "",
    var description_loc: String = "",
    var tl: String = "",
    var difficulty: String = "",
    var specialization: String = "",
    var points: String = "",
    var reference: String = "",
    var parry: String = "",
    @Ignore //todo ???
    var categories: List<String> = emptyList(),
    @Ignore//todo delete
    var default: List<Default> = emptyList()
)