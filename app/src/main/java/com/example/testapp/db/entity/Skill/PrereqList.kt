package com.example.testapp.db.entity.Skill

data class PrereqList(
    var all: Boolean = true,

    var skillPrereqList: List<SkillPrereq> = emptyList(),
    //var advantagePrereqList: List<SkillPrereq> = emptyList(),
    //var attributePrereqList: List<SkillPrereq> = emptyList(),

    var depth: Int = 0,
    var parent: Int = 0
)