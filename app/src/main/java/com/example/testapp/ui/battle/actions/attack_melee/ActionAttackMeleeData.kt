package com.example.testapp.ui.battle.actions.attack_melee

import com.example.testapp.util.Dice

data class ActionAttackMeleeData (
    var weaponDmg: Dice = Dice(1, 0),
    var handSt: Int = 0,
    var weaponReach: Int = 1,
    var skillValue: Int = 10,
    var isFreehand: Boolean = false,
    var target: String = "Body (0)",//todo with enum?
    var modificator: Int = 0,
    var assessment: String = "No (0)",//todo with enum?
    var attackerPose: String = "Stand (0)",//todo with enum?

    var isAllOutAttackDetermined: Boolean = false,//<--
    var isAllOutAttackDouble: Boolean = false,//todo and this too
    var isAllOutAttackPower: Boolean = false,//<--

    var isMoveAndAttack: Boolean = false,
    var isAttackThroughArmor: Boolean = false,
    var isBadFooting: Boolean = false,
    var isGrappled: Boolean = false,
    var isOffHand: Boolean = false,
    var isDualWeaponAttack: Boolean = false,

    var isDeceptiveAttack: Boolean = false,
    var deceptiveAttackPenalty: Int = -1,

    var isRapidStrike: Boolean = false,
    var isWeaponMaster: Boolean = false,

    var isCloseCombat: Boolean = false,
    var isAttackFromAbowe: Boolean = false,
    var hasBigShield: Boolean = false,

    var isCloseCombatWithShield: Boolean = false,
    var closeCombatShieldDB: Int = 1,

    var visibility: Int = 0,
    var isEnemyOutOfView: Boolean = false,
    var isKnownEnemyPosition: Boolean = false,

    var isRidingAnimalUnderAttack: Boolean = false,
    var isRidingSpeedDifference: Boolean = false
)