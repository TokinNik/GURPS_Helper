package com.example.testapp.db.entity.Skill

data class SkillPrereq(
    var has: Boolean = true,
    var nameCompare: String = "",
    var name: String = "",
    var specializationCompare: String = "",
    var specialization: String = "",
    var levelCompare: String = "",
    var level: String = ""
)