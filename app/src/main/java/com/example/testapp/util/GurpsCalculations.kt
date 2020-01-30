package com.example.testapp.util

import com.example.testapp.db.entity.Character

class GurpsCalculations {

    fun mathMove(character: Character): Int {
        return (character.ht + character.dx) / 4 + character.speed + character.move
    }

    fun mathSpeed(character: Character): Float {
        return (character.ht + character.dx) / 4f + character.speed
    }

    fun mathHP(character: Character): Int {
        return character.hp + character.st
    }

    fun mathFP(character: Character): Int {
        return character.fp + character.st
    }

    fun mathWill(character: Character): Int {
        return character.will + character.iq
    }

    fun mathPer(character: Character): Int {
        return character.per + character.iq
    }

    fun mathMeleBaseDamageThr(character: Character): Dice {
        return MeleDamage.THRUST.dmg(character.st)
    }

    fun mathMeleBaseDamageSwg(character: Character): Dice {
        return MeleDamage.SWING.dmg(character.st)
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