package com.example.testapp.db.entity.Skill

import com.example.testapp.db.entity.advantage.AdvantagePrereq

data class PrereqList(
    var all: Boolean = true,

    var skillPrereqList: List<SkillPrereq> = emptyList(),
    var advantagePrereqList: List<AdvantagePrereq> = emptyList(),
//    var attributePrereqList: List<AttributePrereq> = emptyList(),

    var depth: Int = 0,
    var parent: Int = 0
)