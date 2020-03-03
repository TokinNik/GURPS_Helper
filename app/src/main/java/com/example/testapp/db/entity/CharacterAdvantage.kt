package com.example.testapp.db.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.testapp.db.entity.advantage.Modifier

@Entity(
    tableName = "Character_advantage",
    foreignKeys = [
    ForeignKey(entity = Character::class, parentColumns = ["id"], childColumns = ["character_id"], onDelete = CASCADE)
    ],
    primaryKeys = ["character_id", "advantage_name"])
data class CharacterAdvantage (
    @ColumnInfo(name = "character_id")
    var characterId: Int = 0,
    @ColumnInfo(name = "advantage_name")
    var advantageName: String = "",
    var container: String = "empty",
    var levels: String = "",
    var modifiers: List<Modifier> = emptyList()
    )