package com.example.testapp.ui.battle.actions.attack_range

import com.example.testapp.ui.battle.actions.*
import com.example.testapp.util.Dice

class ActionAttackRangeData(private val onItemCheck: () -> Unit) {

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
            CheckModificator("Optic", false, 0),
            CheckModificator("Weapon bearing", false, 1),
            CheckModificator("Target pose: Kneeling | Sitting | Crouching", false, -2),
            CheckModificator("All-Out Attack: Accurate", false, 1),
            CheckModificator("Move And Attack (-Bulk if without aiming )", false, -2),//todo
            CheckModificator("Attack Through Armor Body", false, -8),
            CheckModificator("Attack Through Armor Other", false, -10),
            CheckModificator("Close Combat (-Bulk)", false, -2),//todo
            CheckModificator("Bad Footing", false, -2),
            CheckModificator("Off-Hand", false, -4),
            CheckModificator("Dual-Weapon Attack", false, -4),
            CheckModificator("Pop-up attack", false, -2),
            CheckModificator("Target covered", false, -4),//todo
            CheckModificator("Shooting throw light cover", false, -2),
            CheckModificator("Unknown weapon or system", false, -2),
            CheckModificator("Enemy Out Of View", false, -6),
            CheckModificator("Known Enemy Position", false, -4),
            CheckModificator("Shooting on possible", false, -2),//todo
            CheckModificator("Check target bwfore shooting", false, -2),
            CheckModificator("Hanging outside, shoots under / over vehicles", false, -6),
            CheckModificator("Turn in the saddle / seat, shoot back", false, -4),
            CheckModificator("the transport evaded the last turn, not the pilot", false, -2),
            CheckModificator("Flying vehicles declined in the last move, not the pilot", false, -4),
            CheckModificator("Driving on a good road, the weapon is not on the turret", false, -1),
            CheckModificator("Driving on a bad road, the weapon is not on the turret", false, -3),
            CheckModificator("Driving on a bad road, external mount", false, -2),
            CheckModificator("Driving on a bad road, a turret without a stabilizer", false, -1),
            CheckModificator("Off-road driving, weapons not on turrets", false, -4),
            CheckModificator("Off-road driving, external mount", false, -3),
            CheckModificator("Off-road driving, turret without stabilizer", false, -2),
            CheckModificator("Off-road driving, turret with stabilizer", false, -1),
            CheckModificator("The water is calm, the weapon is not on the turret", false, -3),
            CheckModificator("Water is calm, external mount", false, -2),
            CheckModificator("The water is calm, the turret without a stabilizer", false, -1),
            CheckModificator("Water, waves, weapons not on the turret", false, -4),
            CheckModificator("Water, waves, external mount", false, -3),
            CheckModificator("Water, waves, turret without stabilizer", false, -2),
            CheckModificator("Water, waves, turret with stabilizer", false, -1),
            CheckModificator("Flying equipment, weapons not on the turret", false, -1),
            CheckModificator("Penalty for insufficient ST", false, 0)
        )
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
            else -> println()
        }
    }

    fun getStringListWithEnum(spinnerEnumType: SpinnerEnumType): List<String> {
        return when(spinnerEnumType) {
            SpinnerEnumType.ASSESSMENT -> Assessment.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            SpinnerEnumType.ATTACK_POSE -> AttackPose.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            SpinnerEnumType.ATTACK_TARGET -> AttackTarget.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            SpinnerEnumType.ENVIRONMENT_VISIBILITY -> EnvironmentVisibility.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            else -> emptyList()
        }
    }
}