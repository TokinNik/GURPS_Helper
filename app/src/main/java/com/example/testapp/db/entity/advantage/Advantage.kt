package com.example.testapp.db.entity.advantage

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testapp.db.entity.Skill.PrereqList

@Entity(tableName = "advantage")
data class Advantage (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    //Meta info
    var container: String = "empty",
    var version: String = "2",
    //Main info
    var name: String = "",
    var nameLoc: String = "",
    var descriptionLoc: String = "",
    var type: String = "",
    var levels: String = "",
    var pointsPerLevel: String = "",
    var basePoints: String = "0",
    var reference: String = "",
    var notes: String = "",
    var modifiers: List<Modifier> = emptyList(),
    var categories: List<String> = emptyList(),
    var skillBonuses: List<SkillBonus> = emptyList(),//todo
    var attributeBonuses: List<AttributeBonus> = emptyList(),//todo
    var prereqList: List<PrereqList> = emptyList()
)