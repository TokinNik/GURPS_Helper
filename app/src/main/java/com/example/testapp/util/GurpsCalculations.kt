package com.example.testapp.util

import com.example.testapp.db.entity.Character
import com.example.testapp.ui.character.edit.pages.BindingCharacter
import javax.inject.Inject

class GurpsCalculations @Inject constructor() {

    fun mathMove(move: Int, speed: Float, ht: Int, dx: Int): Int {
        return ((ht + dx) / 4 + speed + move).toInt()
    }

    fun mathSpeed(speed: Float, ht: Int, dx: Int): Float {
        return (ht + dx) / 4f + speed
    }

    fun mathHP(hp: Int, st: Int): Int {
        return hp + st
    }

    fun mathFP(fp: Int, st: Int): Int {
        return fp + st
    }

    fun mathWill(will: Int, iq: Int): Int {
        return will + iq
    }

    fun mathPer(per: Int, iq: Int): Int {
        return per + iq
    }

    fun mathMeleBaseDamageThr(character: Character): Dice {
        return MeleDamage.THRUST.dmg(character.st)
    }

    fun mathMeleBaseDamageSwg(character: Character): Dice {
        return MeleDamage.SWING.dmg(character.st)
    }

    fun reMath(character: BindingCharacter): Int {

        return mathMainStats(character)
    }

    private fun mathMainStats(character: BindingCharacter): Int {
        var total = character.totalPoints
        total -= ((character.st - 10) * 10)
        total -= ((character.dx - 10) * 20)
        total -= ((character.iq - 10) * 20)
        total -= ((character.ht - 10) * 10)
        total -= (character.realHp * 2)
        total -= (character.realPer * 5)
        total -=(character.realWill * 5)
        total -= (character.realFp * 3)
        total -= (character.realSpeed/0.25f * 5).toInt()
        total -= (character.realMove * 5)
        return total
    }

    fun getReMathCharacter(character: Character): Character {
        character.apply {
            hp = mathHP(hp, st)
            per = mathPer(per, iq)
            will = mathWill(will, iq)
            move = mathMove(move, speed, ht, dx)
            speed = mathSpeed(speed, ht, dx)
            fp = mathFP(fp, st)
        }
        return character
    }

    enum class MeleDamage {
        THRUST{
            override fun dmg(st: Int): Dice {
                return when(st){
                    1, 2 -> Dice(1, -6)//1k-6
                    3, 4 -> Dice(1, -5)//1k-5
                    5, 6 -> Dice(1, -4)//1k-4
                    7, 8 -> Dice(1, -3)//1k-3
                    9, 10 -> Dice(1, -2)//1k-2
                    in 11..50 -> Dice(((st - 3) / 8), (((st - 3) % 8) / 2) - 1)//((st - 3) / 8)k + (((st - 3) % 8) / 2) - 1
                    else -> Dice(st / 10 + 1)
                }
            }
        },
        SWING{
            override fun dmg(st: Int): Dice {
                return Dice(1, 0)
            }
        };

        abstract fun dmg(st: Int): Dice
    }
}