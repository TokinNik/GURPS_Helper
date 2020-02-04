package com.example.testapp.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "character")
data class Character(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    //other
    var name: String = "",
    var playerName: String = "",
    var world: String = "",
    var state: String = "",
    var age: String = "",
    var eyes: String = "",
    var hairs: String = "",
    var skin: String = "",
    var race: String = "",
    var gender: String = "",
    var description: String = "",
    var portrait: String = "",
    //characteristics
    var height: String = "",
    var weight: String = "",
    var mainHand: String = "",
    var tl: String = "",
    var sm: String = "",
    var totalPoints: Int = 0,
    var earnPoints: Int = 0,
    var disadvPoints: Int = 0,
    var quirksPoints: Int = 0,
    //base stats
    var st: Int = 10,
    var dx: Int = 10,
    var iq: Int = 10,
    var ht: Int = 10,
    var hp: Int = 0,
    var move: Int = 0,
    var speed: Int = 0,
    var will: Int = 0,
    var per: Int = 0,
    var fp: Int = 0,
    //dynamic stats
    var wounds: String = "",
    var fpLoss: String = "",
    var currentLoad: Int = 0,

    var drScull:Int = 2,//todo split in other classes
    var drFace:Int = 0,
    var drEye:Int = 0,
    var drNeck:Int = 0,
    var drBody:Int = 0,
    var drGroin:Int = 0,
    var drHand:Int = 0,
    var drWrist:Int = 0,
    var drLeg:Int = 0,
    var drFoot:Int = 0,
    //------------------
    var advantages: List<Int> = emptyList(),
    var disadvantages: List<Int> = emptyList(),
    var quirks: List<Int> = emptyList(),
    var inventory: List<Int> = emptyList()
)