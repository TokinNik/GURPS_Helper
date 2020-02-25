package com.example.testapp.ui.battle.actions.defence

import com.example.testapp.ui.battle.actions.*

class ActionDefenceData(private val onItemCheck: () -> Unit) {

    var result: Int = 10
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
    var shield: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var parryNAttackAtRound: Int = 1
        set(value) {
            result += field - value
            field = value
            onItemCheck.invoke()
        }
    var defendingPose: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var positionHeightDifference: Int = 0
        set(value) {
            result -= field
            field = value
            result += value
            onItemCheck.invoke()
        }
    var otherModifications: List<CheckModificator> = emptyList()

    init {
        otherModifications = listOf(
            CheckModificator("Shot from a firearm", false, 0),
            CheckModificator("Attacked by flail", false, -4),
            CheckModificator("Attacked by nunchucks", false, -2),
            CheckModificator("Parry with an arms attack with my bare hands", false, -3),
            CheckModificator("Parry weapons with bare hands attack", false, 0),
            CheckModificator("Retreat", false, 3),
            CheckModificator("Drop down", false, 3),
            CheckModificator("Defending is stunned", false, -4),
            CheckModificator("Side Attack or Envelope", false, -2),
            CheckModificator("Double attack, both hit the same target", false, -1),
            CheckModificator("The defender does not see the attacker", false, -4),
            CheckModificator("Parry throwing weapon", false, -1),
            CheckModificator("Parry throwing weapon (small)", false, -2),
            CheckModificator("The attacker uses a laser sight", false, 1)
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
        onItemCheck.invoke()
    }

    fun onItemSelected(selectedItemPosition: Int, spinnerEnumType: SpinnerEnumType) {
        when(spinnerEnumType) {
            SpinnerEnumType.DEFENCE_POSE -> defendingPose = DefencePose.values()[selectedItemPosition].modification
            SpinnerEnumType.DEFENCE_SHIELD -> shield = DefenceShield.values()[selectedItemPosition].modification
            SpinnerEnumType.DEFENCE_POSITION_HIGHT_DIFFRENCE -> positionHeightDifference = DefencePositionHeightDifferent.values()[selectedItemPosition].modification
            else -> println()
        }
    }

    fun getStringListWithEnum(spinnerEnumType: SpinnerEnumType): List<String> {
        return when(spinnerEnumType) {
            SpinnerEnumType.DEFENCE_POSE -> DefencePose.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            SpinnerEnumType.DEFENCE_SHIELD -> DefenceShield.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            SpinnerEnumType.DEFENCE_POSITION_HIGHT_DIFFRENCE -> DefencePositionHeightDifferent.values().map { "${it.clearName} ${if(it.modification > 0 ) "(+${it.modification})" else "(${it.modification})"}" }
            else -> emptyList()
        }
    }
}