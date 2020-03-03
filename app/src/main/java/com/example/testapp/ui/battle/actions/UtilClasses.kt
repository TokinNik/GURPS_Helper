package com.example.testapp.ui.battle.actions

class CheckModificator(
    var name: String = "Modificator",
    var isChecked: Boolean = false,
    var modification: Int = 0,
    var chainedMod: CheckModificator? = null,
    var onCheckChanged: (check: Boolean) -> Unit = {},
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

enum class SpinnerEnumType {
    ASSESSMENT,
    ATTACK_POSE,
    ATTACK_TARGET,
    ATTACK_TARGET_SIZE,
    ATTACK_AIMING,
    ATTACK_OPTIC_TYPE,
    ATTACK_SHOOTING_POSSIBLE,
    DEFENCE_POSE,
    DEFENCE_SHIELD,
    DEFENCE_POSITION_HIGHT_DIFFRENCE,
    ATTACK_POSITION_HIGHT_DIFFRENCE,
    ENVIRONMENT_VISIBILITY;
}

enum class OpticType(val clearName: String, val modification: Int) {
    FIXED_SCALE("Fixed scale", 0),
    DYNAMIC_SCALE("Dynamic scale", 0);
}

enum class TargetSize(val clearName: String, val modification: Int) {
    SAME_HUMAN("Same human", 0),
    CUSTOM_SIZE("Set custom size", 0),
    SM_SIZE("Set target SM", 0);
}

enum class Aiming(val clearName: String, val modification: Int) {
    NO("No", 0),
    ONE_ROUND("1 round (+Acc)", 0),
    TWO_ROUNDS("2 rounds (+Acc+1)", 1),
    THREE_ROUNDS("3 rounds (+Acc+2)", 2),
    MORE_ROUNDS("3+ rounds (+Acc+2)", 2);
}

enum class ShootingPossible(val clearName: String, val modification: Int) {
    ONE_CELL("view 1 cell", 0),
    TWO_CELL("view 2 cell", -1),
    THREE_FOUR__CELL("view 3-4 cell", -2),
    FIVE_SIX__CELL("view 5-6 cell", -3),
    SEVEN_TEN_CELL("view 7-10 cell", -4),
    TEM_AND_MORE_CELL("view 10+ cell", -5),
    LINE_OF_CELLS("view line of cells", -2),
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

enum class DefencePose(val clearName: String, val modification: Int) {
    STAND("Stand", 0),
    KNEELING_ETC("Kneeling | Sitting | Crouching", -2),
    LYING_DOWN ("Lying down", -3);
}

enum class DefenceShield(val clearName: String, val modification: Int) {
    NO("No", 0),
    SMALL_DB1("Small | Improvised | Cloak (DB1)", 1),
    MEDIUM_DB2("Medium | Heavy Cloak (DB2)", 2),
    LARGE_DB3("Big | Force Shield (DB3)", 3),
    MAGIC_DB4("Magic shield (DB4)", 4);
}

enum class DefencePositionHeightDifferent(val clearName: String, val modification: Int) {
    SAME("Defending and attacking at the same height", 0),
    ABOVE_1("Defending above the attacker (+0.9 m)", 1),
    ABOVE_2("Defending above the attacker (+1.2 m)", 2),
    ABOVE_3("Defending above the attacker (+1.5 m)", 3),
    BELOW_1("Defending below the attacker (-1.5 m)", -1),
    BELOW_2("Defending below the attacker (-1.2 m)", -2),
    BELOW_3("Defending below the attacker (-0.9 m)", -3);
}

enum class AttackerPositionHeightDifferent(val clearName: String, val modification: Int) {
    SAME("Defending and attacking at the same height", 0),
    ABOVE("Defending above the attacker", 0),
    BELOW("Defending below the attacker", 0),
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