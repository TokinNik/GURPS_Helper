package com.example.testapp.db.dao

import androidx.room.*
import com.example.testapp.db.entity.CharacterSkills
import io.reactivex.Single

@Dao
interface CharacterSkillsDao {

    @Query("SELECT * FROM skill, character_skills WHERE character_id = :characterId AND skill.name = character_skills.skill_name")
    fun getCharacterSkills(characterId: Int): Single<List<CharacterSkills>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(skill: CharacterSkills)

    @Update
    fun update(skill: CharacterSkills)

    @Delete
    fun delete(skill: CharacterSkills)

}