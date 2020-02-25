package com.example.testapp.ui.battle.actions.attack_melee

import com.example.testapp.util.Dice
import kotlin.reflect.KClass

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

enum class SpinnerEnumType {
    ASSESSMENT,
    ATTACK_POSE,
    ATTACK_TARGET,
    ENVIRONMENT_VISIBILITY;
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

enum class Assessment(val clearName: String, val modification: Int) {
    NO("No", 0),
    ONE_ROUND("1 round", 1),
    TWO_ROUNDS("2 rounds", 2),
    THREE_ROUNDS("3 rounds", 3);
}

enum class AttackPose(val clearName: String, val modification: Int) {
    STAND("Stand", 0),
    KNEELING_ETC("Kneeling | Sitting | Crouching", -2),
    LYING_DOWN ("Lying down (reach C weapon only)", -4);
}

enum class AttackTarget(val clearName: String, val modification: Int) {
    BODY("Body", 0),
    RANDOM("Random", 0),
    HAND("Hand", -2),
    SHIELD_HAND("Shield hand", -4),
    LEG("Leg", -2),
    FOOT("Foot", -4),
    WRIST("Wrist", -4),
    SHIELD_WRIST("Shield wrist", -8),
    SKULL("Skull", -7),
    FACE("Face", -8),
    NECK("Neck", -5),
    GROIN("Groin", -3),
    EYE("Eye", -9),
    VITAL_ORGANS("Vital organs", -3),
    WING("Wing", -2),
    TAIL("Tail", -3),
    WEAPON_C("Weapon (reach C)", -5),
    WEAPON_1("Weapon (reach 1)", -4),
    WEAPON_2("Weapon (reach 2)", -3);
}

enum class EnvironmentVisibility(val clearName: String, val modification: Int) {
    NORMAL("Normal", 0),
    LOW_1("Low", -1),
    LOW_2("Low", -2),
    LOW_3("Low", -3),
    BAD_1("Bad", -4),
    BAD_2("Bad", -5),
    BAD_3("Bad", -6),
    DARK_1("Dark", -7),
    DARK_2("Dark", -8),
    DARK_3("Dark", -9),
    FULL_DARK("Full dark", -10);
}