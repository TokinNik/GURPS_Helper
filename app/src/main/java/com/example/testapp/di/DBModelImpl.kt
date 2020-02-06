package com.example.testapp.di

import android.app.Application
import androidx.room.Room
import com.example.testapp.db.MainDatabase
import com.example.testapp.db.entity.CharacterSkills
import com.example.testapp.db.entity.Skill.Skill
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

                }
            ).let(compositeDisposable::add)
        compositeDisposable.clear()
    }

    override fun saveCharacterAdvantage(adv: List<CharacterSkills>, characterId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveCharacterDisadvantage(disadv: List<CharacterSkills>, characterId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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