package com.example.testapp.ui.battle.actions.attack_melee

import com.example.testapp.ui.battle.actions.*
import com.example.testapp.util.Dice

class ActionAttackMeleeDataV2(private val onItemCheck: () -> Unit) {

    var result: Int = 10
    var weaponDmg: Dice = Dice(1, 0)
    var handSt: Int = 0
    var weaponSt: Int = 0
        set(value) {
            field = value
            otherModifications.last().modification = handSt - value
        }
    var stPenalty: Boolean = false
        get() {
        return handSt < weaponSt
    }
    var weaponReach: Int = 1
    var skillValue: Int = 10
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var target: Int = 0
    set(value) {
        result -= field
        field = value
        result += value
        onItemCheck.invoke()
    }
    var modification: Int = 0
    set(value) {
        result -= field
        field = value
        result += value
        onItemCheck.invoke()
    }
    var assessment: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var attackerPose: Int = 0
    set(value) {
        result -= field
        field = value
        result += value
        onItemCheck.invoke()
    }
    var visibility: Int = 0
    set(value) {
        result -= field
        field = value
        result += value
        onItemCheck.invoke()
    }
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
            result += field - value
            field = value
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
        otherModifications.first{ it.name == "Rapid Strike" }.chainedMod = otherModifications.first{ it.name == "Weapon Master" }
    }

    fun onItemChecked(name: String) {
        otherModifications.first { it.name == name }
            .apply {
                isChecked = !isChecked
                if (chainedMod != null && !isChecked) {
                    chainedMod!!.isChecked = false
                    result = result - chainedMod!!.modification - chainedMod!!.secondMod
                }
                result = if (isChecked)
                    result + modification + secondMod
                else
                    result - modification - secondMod
            }
        onItemCheck.invoke()//todo work without it in page_character_edit_stat, why???
    }

    fun onItemSelected(selectedItemPosition: Int, spinnerEnumType: SpinnerEnumType) {
        when(spinnerEnumType) {
            SpinnerEnumType.ASSESSMENT -> assessment = Assessment.values()[selectedItemPosition].modification
            SpinnerEnumType.ATTACK_POSE -> attackerPose = AttackPose.values()[selectedItemPosition].modification
            SpinnerEnumType.ATTACK_TARGET -> target = AttackTarget.values()[selectedItemPosition].modification
            SpinnerEnumType.ENVIRONMENT_VISIBILITY -> visibility = EnvironmentVisibility.values()[selectedItemPosition].modification
        }
    }

    fun getStringListWithEnum(spinnerEnumType: SpinnerEnumType): List<String> {
        return when(spinnerEnumType) {
            SpinnerEnumType.ASSESSMENT -> Assessment.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            SpinnerEnumType.ATTACK_POSE -> AttackPose.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            SpinnerEnumType.ATTACK_TARGET -> AttackTarget.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            SpinnerEnumType.ENVIRONMENT_VISIBILITY -> EnvironmentVisibility.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
        }
    }
}