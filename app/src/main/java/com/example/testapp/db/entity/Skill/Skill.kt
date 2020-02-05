package com.example.testapp.db.entity.Skill

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "skill")
data class Skill(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @Ignore
    var container: String = "empty",
    var name: String = "",
    var nameLoc: String = "",
    var descriptionLoc: String = "",
    var tl: String = "",
    var difficulty: String = "",
    var specialization: String = "",
    var points: String = "",
    var reference: String = "",
    var parry: String = "",
    var categories: List<String> = emptyList(),
    var defaults: List<Default> = emptyList(),
    var prereqList: List<PrereqList> = emptyList()

)