package com.example.testapp.ui.battle.actions

class CheckModificator(
    var name: String = "Modificator",
    var isChecked: Boolean = false,
    var modification: Int = 0,
    var chainedMod: CheckModificator? = null,
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
    ENVIRONMENT_VISIBILITY;
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