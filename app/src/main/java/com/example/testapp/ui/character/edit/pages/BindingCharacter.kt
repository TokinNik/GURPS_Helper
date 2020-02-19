package com.example.testapp.ui.character.edit.pages

import com.example.testapp.db.entity.Character
import com.example.testapp.util.GurpsCalculations
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

class BindingCharacter(character: Character, private val reMath: ()-> Unit) {

    private val gurpsCalculations: GurpsCalculations by inject()

    private var isRe = false

    var totalPoints: Int = 100
    var earnPoints: Int = 0
        set(value) {
            field = value
            println("SET EARN_POINTS - $value")
            reMath.invoke()
        }
    var disadvPoints: Int = 0
    var quirksPoints: Int = 0
    //base stats

    var st: Int = 0
        set(value) {
            field = value
            println("SET ST - $value")
            xreMathPoints()
        }

    var dx: Int = 0
        set(value) {
            field = value
            println("SET DX - $value")
            xreMathPoints()
        }

    var iq: Int = 0
        set(value) {
            field = value
            println("SET IQ - $value")
            xreMathPoints()
        }
    var ht: Int = 0
        set(value) {
            field = value
            println("SET HT - $value")
            xreMathPoints()
        }
    var realHp: Int = 0
    var hp: Int = 0
        set(value) {
            if (isRe)
            realHp += value - field
            field = gurpsCalculations.mathHP(realHp, st)
            xreMathPoints()
        }
    var realMove: Int = 0
    var move: Int = 0
        set(value) {
            if (isRe)
                realMove += value - field
            field = gurpsCalculations.mathMove(realMove, realSpeed, ht, dx)
            xreMathPoints()
        }
    var realSpeed: Float = 0f
    var speed: Float = 0f
        set(value) {
            if (isRe)
                realSpeed += value - field
            field = gurpsCalculations.mathSpeed(realSpeed, ht, dx)
            xreMathPoints()
        }
    var realWill: Int = 0
    var will: Int = 0
        set(value) {
            if (isRe)
                realWill += value - field
            field = gurpsCalculations.mathWill(realWill, iq)
            xreMathPoints()
        }
    var realPer: Int = 0
    var per: Int = 0
        set(value) {
            if (isRe)
                realPer += value - field
            field = gurpsCalculations.mathPer(realPer, iq)
            xreMathPoints()
        }
    var realFp: Int = 0
    var fp: Int = 0
        set(value) {
            if (isRe)
                realFp += value - field
            field = gurpsCalculations.mathFP(realFp, st)
            xreMathPoints()
        }

    //------------------
    init {
        val scope = Toothpick.openScope("APP")
        scope.inject(this)

        st = character.st
        dx = character.dx
        iq = character.iq
        ht = character.ht

        hp = gurpsCalculations.mathHP(character.hp, st)
        realHp = character.hp

        per = gurpsCalculations.mathPer(character.per, iq)
        realPer = character.per

        will = gurpsCalculations.mathWill(character.will, iq)
        realWill = character.will

        fp = gurpsCalculations.mathFP(character.fp, st)
        realFp = character.fp

        move = gurpsCalculations.mathMove(character.move, character.speed, ht, dx)
        realMove = character.move

        speed = gurpsCalculations.mathSpeed(character.speed, ht, dx)
        realSpeed = character.speed

        totalPoints = character.totalPoints
        earnPoints = character.earnPoints
        isRe = true
    }

    private fun xreMathPoints() {
        if (isRe)
            earnPoints = gurpsCalculations.reMath(this)
    }
}