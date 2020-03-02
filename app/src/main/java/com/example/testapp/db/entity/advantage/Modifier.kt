package com.example.testapp.db.entity.advantage

data class Modifier (
    var version: String = "1",
    var enabled: Boolean = false,
    var name: String = "",
    var cost: Int = 0,
    var costType: String = "points",
    var affects: String = "",
    var reference: String = ""
)