package com.example.testapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character")
data class Character(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    //other
    var name: String = "chName",
    var playerName: String = "playerName",
    var world: String = "world",
    var state: String = "-",
    var age: String = "0",
    var eyes: String = "-",
    var hairs: String = "-",
    var skin: String = "-",
    var race: String = "human",
    var gender: String = "male",
    var description: String = "-",
    //characteristics
    var height: String = "0",
    var weight: String = "0",
    var mainHand: String = "right",
    var tl: String = "0",
    var sm: String = "0",
    var totalPoints: Int = 0,
    var earnPoints: Int = 0,
    var disadvPoints: Int = 0,
    var quirksPoints: Int = 0,
    //base stats
    var st: Int = 10,
    var dx: Int = 10,
    var iq: Int = 10,
    var ht: Int = 10,
    var hp: Int = 10,
    var move: Int = 5,
    var speed: Int = 5,
    var will: Int = 10,
    var per: Int = 10,
    var fp: Int = 10,
    //dynamic stats
    var wounds: Int = 0,
    var fpLoss: Int = 0,
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
    var skills: List<Int> = emptyList(),
    var advantages: List<Int> = emptyList(),
    var disadvantages: List<Int> = emptyList(),
    var quirks: List<Int> = emptyList(),
    var inventory: List<Int> = emptyList()
)