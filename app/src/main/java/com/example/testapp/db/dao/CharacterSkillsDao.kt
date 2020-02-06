package com.example.testapp.db.dao

import androidx.room.*
import com.example.testapp.db.entity.CharacterSkills
import io.reactivex.Single

@Dao
interface CharacterSkillsDao {

    @Query("SELECT * FROM character_skills WHERE character_id = :characterId")
    fun getCharacterSkills(characterId: Int): Single<List<CharacterSkills>>

    @Query("SELECT * FROM character_skills")
    fun getAll(): Single<List<CharacterSkills>>

    @Query("DELETE FROM character_skills WHERE character_id = :characterId")
    fun deleteAllById(characterId: Int)

    @Query("DELETE FROM character_skills")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(skill: CharacterSkills)

    @Update
    fun update(skill: CharacterSkills)

    @Delete
    fun delete(skill: CharacterSkills)
}