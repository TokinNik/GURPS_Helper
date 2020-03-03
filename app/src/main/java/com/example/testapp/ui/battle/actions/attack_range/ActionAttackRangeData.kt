package com.example.testapp.ui.battle.actions.attack_range

import com.example.testapp.ui.battle.actions.*
import com.example.testapp.util.Dice

class ActionAttackRangeData(private val onItemCheck: () -> Unit) {

    var result: Int = 10
    var weaponDmg: Dice = Dice(2, 0)
    var handSt: Int = 0
    var acc: Int = 1
    var range: Int = 100
    var rangeMax: Int = 500
    var rangeOut: String
        get() {
            return "$range/$rangeMax"
        }
        set(value) {
            value.split('/').apply {
                range = first().toInt()
                rangeMax = last().toInt()
            }
        }
    var rof: Int = 1
    var bulk: Int = -1
    var rcl: Int = 1

    var weaponSt: Int = 0
        set(value) {
            field = value
            otherModifications.last().modification = handSt - value
        }
    var stPenalty: Boolean = false
        get() {
        return handSt < weaponSt
    }
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
    var distance: Int = 0
    set(value) {
        result -= field
        field = value
        result += value
        onItemCheck.invoke()
    }
    var targetSpeedM: Int = 0
    set(value) {
        result -= field
        field = value
        result += value
        onItemCheck.invoke()
    }
    var targetSpeedKM: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var shootsCount: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var opticType: Int = 0
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
    var targetSM: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var targetPositionHeightDifferent: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var aiming: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var positionDifference: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var targetSize: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var shootingPossible: Int = 0
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
            CheckModificator("Enemy Out Of View, but you known his position", false, -4),
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
            SpinnerEnumType.ATTACK_AIMING -> aiming = Aiming.values()[selectedItemPosition].modification
            SpinnerEnumType.ATTACK_TARGET -> target = AttackTarget.values()[selectedItemPosition].modification
            SpinnerEnumType.ATTACK_TARGET_SIZE -> targetSize = TargetSize.values()[selectedItemPosition].modification
            SpinnerEnumType.ATTACK_OPTIC_TYPE -> opticType = OpticType.values()[selectedItemPosition].modification
            SpinnerEnumType.ATTACK_SHOOTING_POSSIBLE -> shootingPossible = ShootingPossible.values()[selectedItemPosition].modification
            SpinnerEnumType.ATTACK_POSITION_HIGHT_DIFFRENCE -> positionDifference = AttackerPositionHeightDifferent.values()[selectedItemPosition].modification
            SpinnerEnumType.ENVIRONMENT_VISIBILITY -> visibility = EnvironmentVisibility.values()[selectedItemPosition].modification
            else -> println()
        }
    }

    fun getStringListWithEnum(spinnerEnumType: SpinnerEnumType): List<String> {
        return when(spinnerEnumType) {
            SpinnerEnumType.ATTACK_AIMING -> Aiming.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification + acc})" else "(+${acc})"}" }
            SpinnerEnumType.ATTACK_TARGET -> AttackTarget.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            SpinnerEnumType.ATTACK_TARGET_SIZE -> TargetSize.values().map { it.clearName }
            SpinnerEnumType.ATTACK_OPTIC_TYPE -> OpticType.values().map { it.clearName }
            SpinnerEnumType.ATTACK_SHOOTING_POSSIBLE -> ShootingPossible.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            SpinnerEnumType.ATTACK_POSITION_HIGHT_DIFFRENCE -> AttackerPositionHeightDifferent.values().map { it.clearName }
            SpinnerEnumType.ENVIRONMENT_VISIBILITY -> EnvironmentVisibility.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            else -> emptyList()
        }
    }
}