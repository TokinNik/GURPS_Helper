package com.example.testapp.db.dao

import androidx.room.*
import com.example.testapp.db.entity.Skill
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface SkillDao {

    @Query("SELECT * FROM skill")
    fun getAll(): Flowable<List<Skill>>

    @Query("SELECT * FROM skill WHERE id = :id")
    fun getById(id: Int): Single<Skill>

    @Query("SELECT * FROM skill WHERE id IN (:id)")
    fun getByIds(id: List<Int>): Single<List<Skill>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(character: Skill)

    @Update
    fun update(character: Skill)

    @Delete
    fun delete(character: Skill)

}