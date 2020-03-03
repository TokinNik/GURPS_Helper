package com.example.testapp.db.dao

import androidx.room.*
import com.example.testapp.db.entity.CharacterAdvantage
import com.example.testapp.db.entity.CharacterSkills
import io.reactivex.Single

@Dao
interface CharacterAdvantageDao {

    @Query("SELECT * FROM character_advantage WHERE character_id = :characterId")
    fun getCharacterAdvantages(characterId: Int): Single<List<CharacterAdvantage>>

    @Query("SELECT * FROM character_advantage")
    fun getAll(): Single<List<CharacterAdvantage>>

    @Query("DELETE FROM character_advantage WHERE character_id = :characterId")
    fun deleteAllById(characterId: Int)

    @Query("DELETE FROM character_advantage")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(skill: CharacterAdvantage)

    @Update
    fun update(skill: CharacterAdvantage)

    @Delete
    fun delete(skill: CharacterAdvantage)
}