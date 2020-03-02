package com.example.testapp.db.dao

import androidx.room.*
import com.example.testapp.db.entity.advantage.Advantage
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface AdvantageDao {

    @Query("SELECT * FROM advantage")
    fun getAll(): Flowable<List<Advantage>>

    @Query("SELECT * FROM advantage WHERE id = :id")
    fun getById(id: Int): Single<Advantage>

    @Query("SELECT * FROM advantage WHERE name = :name")
    fun getByName(name: String): Single<Advantage>

    @Query("SELECT * FROM advantage WHERE name LIKE :query")
    fun searchAdvantage(query: String): Single<Advantage>

    @Query("SELECT * FROM advantage WHERE name LIKE :query")
    fun searchAdvantages(query: String): Flowable<List<Advantage>>

    @Query("SELECT * FROM advantage WHERE id IN (:id)")
    fun getByIds(id: List<Int>): Single<List<Advantage>>

    @Query("SELECT * FROM advantage WHERE name IN (:names)")
    fun getByNames(names: List<String>): Single<List<Advantage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(skill: Advantage)

    @Update
    fun update(skill: Advantage)

    @Delete
    fun delete(skill: Advantage)

}