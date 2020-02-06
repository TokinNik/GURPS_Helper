package com.example.testapp.di

import com.example.testapp.db.MainDatabase
import com.example.testapp.db.entity.CharacterSkills
import com.example.testapp.db.entity.Skill.Skill

interface DBModel {

    fun getDB(): MainDatabase

    fun saveCharacterSkills(skills: List<Skill>, characterId: Int)

    fun saveCharacterAdvantage(adv: List<CharacterSkills>, characterId: Int)//todo

    fun saveCharacterDisadvantage(disadv: List<CharacterSkills>, characterId: Int)//todo

}