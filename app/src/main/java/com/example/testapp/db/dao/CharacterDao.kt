package com.example.testapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.testapp.db.SkillsAndCharacterOnSt
import com.example.testapp.db.entity.Character
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    fun getAll(): Flowable<List<Character>>

    @Query("SELECT * FROM character WHERE id = :id")
    fun getById(id: Int): Single<Character>

    @Query("SELECT character.id, character.name, skill.id AS skill_id, skill.name AS skill_name FROM character, skill WHERE character.st > 10 and skill.mainAttribute = :skillAttr")
    fun getCharactersWithSkill(skillAttr: String): Single<List<SkillsAndCharacterOnSt>>//todo delete (test)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(character: Character)

    @Update
    fun update(character: Character)

    @Delete
    fun delete(character: Character)
}