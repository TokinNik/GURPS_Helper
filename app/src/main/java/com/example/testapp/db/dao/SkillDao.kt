package com.example.testapp.db.dao

import androidx.room.*
import com.example.testapp.db.entity.Skill.Skill
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface SkillDao {

    @Query("SELECT * FROM skill")
    fun getAll(): Flowable<List<Skill>>

    @Query("SELECT * FROM skill WHERE id = :id")
    fun getById(id: Int): Single<Skill>

    @Query("SELECT * FROM skill WHERE name = :name")
    fun getByName(name: String): Single<Skill>

    @Query("SELECT * FROM skill WHERE name LIKE :query")
    fun searchSkill(query: String): Single<Skill>

    @Query("SELECT * FROM skill WHERE name LIKE :query")
    fun searchSkills(query: String): Flowable<List<Skill>>

    @Query("SELECT * FROM skill WHERE name = :name AND specialization = :specialization")
    fun getByParams(name: String, specialization: String): Single<Skill>

    @Query("SELECT * FROM skill WHERE id IN (:id)")
    fun getByIds(id: List<Int>): Single<List<Skill>>

    @Query("SELECT * FROM skill WHERE name IN (:names)")
    fun getByNames(names: List<String>): Single<List<Skill>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(skill: Skill)

    @Update
    fun update(skill: Skill)

    @Delete
    fun delete(skill: Skill)

}