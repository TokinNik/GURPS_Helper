package com.example.testapp.di

import android.app.Application
import androidx.room.Room
import com.example.testapp.db.MainDatabase
import com.example.testapp.db.entity.CharacterAdvantage
import com.example.testapp.db.entity.CharacterSkills
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.db.entity.advantage.Advantage
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DBModelImpl @Inject constructor(private val application: Application) : DBModel {

    val db =  Room.databaseBuilder<MainDatabase>(
    application,
    MainDatabase::class.java,
    "database")
//       .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    private val compositeDisposable = CompositeDisposable()

    override fun getDB(): MainDatabase = db

    override fun saveCharacterSkills(skills: List<Skill>, characterId: Int) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            db.characterSkillsDao().deleteAllById(characterId)
            convertAndAddSkills(skills, characterId)
            emitter.onComplete()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                { println(it) },
                {
                    compositeDisposable.clear()
                }
            ).let(compositeDisposable::add)
    }

    override fun saveCharacterAdvantage(adv: List<Advantage>, characterId: Int) {
        Observable.create { emitter: ObservableEmitter<Int> ->
            db.characterAdvantageDao().deleteAllById(characterId)
            convertAndAddAdvantage(adv, characterId)
            emitter.onComplete()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                { println(it) },
                {
                    compositeDisposable.clear()
                }
            ).let(compositeDisposable::add)
    }

    private fun convertAndAddAdvantage(adv: List<Advantage>, characterId: Int) {
        adv.forEach {
            db.characterAdvantageDao().insert(
                CharacterAdvantage(
                    characterId = characterId,
                    advantageName = it.name,
                    container = it.container,
                    levels = it.levels,
                    modifiers = it.modifiers
                )
            )
        }
    }

    private fun convertAndAddSkills(skillsList: List<Skill>, characterId: Int){
        skillsList.forEach {
            db.characterSkillsDao().insert(
                CharacterSkills(
                    characterId = characterId,
                    skillName = it.name,
                    specialization = it.specialization,
                    container = it.container,
                    points = it.points
                )
            )
        }
    }
}