package com.example.testapp.util

import javax.inject.Inject

class RollUtil@Inject constructor(){

    private val diceRange = 6

    fun roll3D6() = (1..diceRange).random() + (1..diceRange).random() + (1..diceRange).random()

    fun roll3D6(negative: Int) = roll3D6() - negative

    fun roll3D6(negative: Int, positive: Int) = roll3D6() - negative + positive

}