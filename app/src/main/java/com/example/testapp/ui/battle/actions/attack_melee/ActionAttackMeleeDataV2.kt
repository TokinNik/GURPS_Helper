package com.example.testapp.ui.battle.actions.attack_melee

import com.example.testapp.util.Dice

class ActionAttackMeleeDataV2(private val onItemCheck: () -> Unit) {

    var result: Int = 10
    var weaponDmg: Dice = Dice(1, 0)
    var handSt: Int = 0
    var weaponReach: Int = 1
    var skillValue: Int = 10
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    //    var isFreehand: Boolean = false,
    var target: String = "Body (0)"//todo with enum?
    var modification: Int = 0
    set(value) {
        result -= field
        field = value
        result += value
        onItemCheck.invoke()
    }
    var assessment: String = "No (0)"//todo with enum?
    var attackerPose: String = "Stand (0)"//todo with enum?
    var visibility: Int = 0//todo with enum?
    var otherModifications: List<CheckModificator> = emptyList()
    var deceptiveAttackPenalty: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var closeCombatShieldDB: Int = 0
        set(value) {
            result += field
            field = value
            result -= value
            onItemCheck.invoke()
        }
    init {
        otherModifications = listOf(
            CheckModificator("Freehand", false, 0),
            CheckModificator("All-Out Attack Determined", false, 4),
            CheckModificator("All-Out Attack Double (+attack)", false, 0),
            CheckModificator("All-Out Attack Power (dmg +2/+1 per dice)", false, 0),
            CheckModificator("Move And Attack", false, -4),
            CheckModificator("Attack Through Armor Body", false, -8),
            CheckModificator("Attack Through Armor Other", false, -10),
            CheckModificator("Bad Footing", false, -2),
            CheckModificator("Grappled", false, -4),
            CheckModificator("Off-Hand", false, -4),
            CheckModificator("Dual-Weapon Attack", false, -4),
            CheckModificator("Deceptive Attack", false, -2) { deceptiveAttackPenalty = it },
            CheckModificator("Rapid Strike", false, -6),
            CheckModificator("Weapon Master", false, 3),
            CheckModificator("Close Combat", false, -2),
            CheckModificator("Wild Swing", false, -5),
            CheckModificator("Attack From Abowe", false, -2),
            CheckModificator("Big Shield", false, -2),
            CheckModificator("Close Combat With Shield", false, -1) { closeCombatShieldDB = it },
            CheckModificator("Enemy Out Of View", false, -6),
            CheckModificator("Known Enemy Position", false, -4),
            CheckModificator("Riding Animal Under Attack", false, -2),
            CheckModificator("Riding Speed Difference 7+", false, -1),
            CheckModificator("Penalty for insufficient ST", false, 0)//todo check character ST adn show if less
        )
    }

    fun onItemChecked(name: String) {
        otherModifications.first { it.name == name }
            .apply {
                isChecked = !isChecked
                result = if (isChecked)
                    result + modification + secondMod
                else
                    result - modification - secondMod
            }
        onItemCheck.invoke()//todo work without it in page_character_edit_stat, why???
    }

    fun getResultModification(): Int {
        var result = 0
        otherModifications
            .filter { it.isChecked }
            .map { it.modification }
            .forEach { result += it }
        result += modification
        result += visibility
        result += deceptiveAttackPenalty
        result += closeCombatShieldDB
//        result += target
//        result += assessment todo
//        result += attackerPose

        return result
    }
}

class CheckModificator(
    var name: String = "Modificator",
    var isChecked: Boolean = false,
    var modification: Int = 0,
    var secondModChanged: (value: Int) -> Unit = {}
) {
    var secondMod: Int = 0
    get() {
        return if (isChecked) field else 0
    }
    set(value) {
        field = value
        secondModChanged.invoke(value)
    }


    fun getFullName() = "$name ${
    if(modification != 0)  
        if(modification > 0 ) "(+$modification)" else "($modification)"
    else ""
    }"
}