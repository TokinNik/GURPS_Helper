package com.example.testapp.ui.battle.actions.defence

import com.example.testapp.ui.battle.actions.*

class ActionDefenceData(private val onItemCheck: () -> Unit) {

    enum class DefenceType {
        DODGE,
        PARRY,
        BLOCK
    }

    var result: Int = 8

    var defenceType: DefenceType = DefenceType.DODGE

    var skillValue: Int = 8
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
    var realParryNAttackAtRound: Int = 4//todo kostil'
    var parryNAttackAtRound: Int = 1
        set(value) {
            result += (field - value) * getParryPenalty()
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
            CheckModificator(
                name = "Attacked by flail",
                isChecked = false,
                modification = -4,
                onCheckChanged = {
                    if (it && otherModifications[2].isChecked)
                        onItemChecked(otherModifications[2].name)
                }),
            CheckModificator(
                name = "Attacked by nunchucks",
                isChecked = false,
                modification = -2,
                onCheckChanged = {
                    if (it && otherModifications[1].isChecked)
                        onItemChecked(otherModifications[1].name)
                }),
            CheckModificator("Parry with an arms attack with my bare hands", false, -3),
            CheckModificator("Parry weapons with bare hands attack", false, 0),
            CheckModificator(
                name = "Defend fencing weapons",
                isChecked = false,
                modification = 0,
                onCheckChanged = {
                        result += (parryNAttackAtRound - 1) * realParryNAttackAtRound
                        realParryNAttackAtRound = getParryPenalty()
                        result -= (parryNAttackAtRound  - 1) * realParryNAttackAtRound
                }),
            CheckModificator(
                name = "Has a “Master Apprentice” or “Weapon Master”",
                isChecked = false,
                modification = 0,
                onCheckChanged = {
                    result += (parryNAttackAtRound - 1) * realParryNAttackAtRound
                    realParryNAttackAtRound = getParryPenalty()
                    result -= (parryNAttackAtRound  - 1) * realParryNAttackAtRound
                }),
            CheckModificator(
                name = "Retreat",
                isChecked = false,
                modification = 3,
                onCheckChanged = {
                    if (it && otherModifications[8].isChecked)
                        onItemChecked(otherModifications[8].name)
                }),
            CheckModificator(
                name = "Drop down",
                isChecked = false,
                modification = 3,
                onCheckChanged = {
                    if (it && otherModifications[7].isChecked)
                        onItemChecked(otherModifications[7].name)
                }),
            CheckModificator("Defending is stunned", false, -4),
            CheckModificator("Side Attack or Envelope", false, -2),
            CheckModificator("Double attack, both hit the same target", false, -1),
            CheckModificator("The defender does not see the attacker", false, -4),
            CheckModificator("Parry throwing weapon", false, -1),
            CheckModificator("Parry throwing weapon (small)", false, -2),
            CheckModificator("The attacker uses a laser sight", false, 1)
        )
    }

    private fun getParryPenalty(): Int = 4 - (//todo ifififififif...
            if (otherModifications[5].isChecked)
                if (otherModifications[6].isChecked) 3 else 2
            else
                if (otherModifications[6].isChecked) 2 else 0
            )

    fun changeDefenceType(type: DefenceType, check: Boolean){
        if (check) {
            defenceType = type
            when(type) {
                DefenceType.DODGE -> {
                    otherModifications[7].modification = 3
                }
                DefenceType.PARRY -> {
                    otherModifications[7].modification = 1
                }
                DefenceType.BLOCK -> {
                    otherModifications[7].modification = 1
                }
            }
        } else {
            when(type) {
                DefenceType.DODGE -> {
                    if (otherModifications[8].isChecked)
                        onItemChecked(otherModifications[8].name)
                }
                DefenceType.PARRY ->{
                    if (otherModifications[3].isChecked)
                        onItemChecked(otherModifications[3].name)
                    if (otherModifications[4].isChecked)
                        onItemChecked(otherModifications[4].name)
                    if (otherModifications[13].isChecked)
                        onItemChecked(otherModifications[13].name)
                    if (otherModifications[14].isChecked)
                        onItemChecked(otherModifications[14].name)
                }
                DefenceType.BLOCK -> {

                }
            }
        }
    }

    fun onItemChecked(name: String) {
        otherModifications.first { it.name == name }
            .apply {
                isChecked = !isChecked
                onCheckChanged.invoke(isChecked)
                if (!isChecked) {
                    if (chainedMod != null) {
                        chainedMod!!.isChecked = false
                        result = result - chainedMod!!.modification - chainedMod!!.secondMod
                    }

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