package com.example.testapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.testapp.db.entity.Character
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface CharacterDao {

    @Query("SELECT * FROM character")
    fun getAll(): Flowable<List<Character>>

    @Query("SELECT * FROM character WHERE id = :id")
    fun getById(id: Int): Single<Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(character: Character)

    @Update
    fun update(character: Character)

    @Delete
    fun delete(character: Character)
}