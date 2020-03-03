package com.example.testapp.util

import android.os.Environment
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.db.entity.advantage.Advantage
import java.io.FileOutputStream
import java.nio.charset.Charset

class GCSXmlBuilder {
    fun xmlTest(
        character: Character,
        skills: List<Skill>,
        advantages: List<Advantage>
    ) =
        startCharacterXML(character.version, character.measure){
            xmlTag("created_date") { +character.createdDate }
            xmlTag("modified_date") { +character.modifiedDate }
            xmlTag("profile") {
                xmlTag("name") { +character.name }
                xmlTag("player_name") { +character.playerName }
                xmlTag("campaign") { +character.world }
                xmlTag("tech_level") { +character.tl }
                xmlTag("title") { +character.state }
                xmlTag("age") { + character.age }
                xmlTag("birthday") { +character.birthday }
                xmlTag("eyes") { +character.eyes }
                xmlTag("hair") { +character.hairs }
                xmlTag("skin") { +character.skin }
                xmlTag("handedness") { +character.mainHand }
                xmlTag("height") { +character.height }
                xmlTag("weight") { +character.weight }
                xmlTag("gender") { +character.gender }
                xmlTag("race") { +character.race }
                xmlTag("religion") { +character.religion}
                xmlTag("sm") { +character.sm }
                xmlTag("notes") { +character.description }
                xmlTag("portrait") { +character.portrait }
            }
            xmlTag("HP") { +character.hp.toString() }
            xmlTag("FP") { +character.fp.toString() }
            xmlTag("total_points") { +character.totalPoints.toString() }
            xmlTag("disadvantages_threshold") { +character.disadvPoints.toString() }
            xmlTag("quirks_threshold") { +character.quirksPoints.toString() }
            xmlTag("ST") { +character.st.toString() }
            xmlTag("DX") { +character.dx.toString() }
            xmlTag("IQ") { +character.iq.toString() }
            xmlTag("HT") { +character.ht.toString() }
            xmlTag("will") { +character.will.toString() }
            xmlTag("perception") { +character.per.toString() }
            xmlTag("speed") { +character.speed.toString() }
            xmlTag("move") { +character.move.toString() }
            xmlAttrTag("hp_lost", Pair("total", character.hpLossTotal)) { +character.hpLoss }
            xmlAttrTag("fp_lost", Pair("total", character.fpLossTotal)) { +character.fpLoss }
            xmlAttrTag("advantage_list", Pair("size", character.advListSize)) {
                var currentContainer = ""
                advantages.forEach {advantage ->
                    currentContainer = advantage.container
                    if (currentContainer != "empty")//todo in enum?
                    {
                        xmlTag("advantage_container") {
                            xmlTag("name") { +currentContainer }
                            xmlAttrTag("advantage", Pair("version", advantage.version)) {
                                xmlTag("name") { +advantage.name }
                                xmlTag("name-loc") { +advantage.nameLoc }
                                xmlTag("description-loc") { +advantage.descriptionLoc }
                                xmlTag("type") { +advantage.type }
                                xmlTag("levels") { +advantage.levels }
                                xmlTag("points_per_level") { +advantage.pointsPerLevel }
                                xmlTag("base_points") { +advantage.basePoints }
                                xmlTag("notes") { +advantage.notes }
                                xmlTag("reference") { +advantage.reference }
                                advantage.categories.forEach {category ->
                                    xmlTag("categories") {
                                        xmlTag("category") { +category }
                                    }
                                }
                                advantage.skillBonuses.forEach {skillBonus ->
                                    xmlTag("skill_bonus") {
                                        xmlAttrTag("name", Pair("compare", skillBonus.nameCompare)) { +skillBonus.name }
                                        xmlAttrTag("specialisation", Pair("compare", skillBonus.specializationCompare)) { +skillBonus.specialization }
                                        xmlTag("amount") { +skillBonus.amount }
                                    }
                                }
                                advantage.attributeBonuses.forEach {attrBonus ->
                                    xmlTag("skill_bonus") {
                                        xmlTag("attribute") { +attrBonus.attribute }
                                        xmlTag("amount") { +attrBonus.amount }
                                    }
                                }
                                advantage.modifiers.forEach {modifier ->
                                    xmlAttr2Tag("modifier", Pair("version", modifier.version), Pair("enabled", if(modifier.enabled) "yes" else "no")) {
                                        xmlTag("name") { +modifier.name }
                                        xmlAttrTag("cost", Pair("type", modifier.costType)) { +modifier.cost }
                                        xmlTag("affects") { +modifier.affects }
                                        xmlTag("reference") { +modifier.reference }
                                    }
                                }
                                advantage.prereqList.forEach {prereqList ->
                                    xmlAttrTag("prereq_list", Pair("all", if (prereqList.all)"yes" else "no")) {
                                        prereqList.skillPrereqList.forEach { skillPrereq ->
                                            xmlAttrTag("skill_prereq", Pair("has", if (skillPrereq.has)"yes" else "no")) {
                                                xmlAttrTag("name", Pair("compare", skillPrereq.nameCompare)) { +skillPrereq.name }
                                                xmlAttrTag("specialization", Pair("compare", skillPrereq.specializationCompare)) { +skillPrereq.specialization }
                                                xmlAttrTag("level", Pair("compare", skillPrereq.levelCompare)) { +skillPrereq.level }
                                            }
                                        }
                                        prereqList.advantagePrereqList.forEach { advPrereq ->
                                            xmlAttrTag("skill_prereq", Pair("has", if (advPrereq.has)"yes" else "no")) {
                                                xmlAttrTag("name", Pair("compare", advPrereq.nameCompare)) { +advPrereq.name }
                                                xmlAttrTag("notes", Pair("compare", advPrereq.notesCompare)) { +advPrereq.notes }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            xmlAttrTag("skill_list", Pair("size", character.skillListSize)) {
                skills.forEach { skill ->
                    xmlAttrTag("skill", Pair("version", skill.version)) {
                        xmlTag("name") { +skill.name }
                        xmlTag("name-loc") { +skill.nameLoc }
                        xmlTag("description-loc") { +skill.descriptionLoc }
                        xmlTag("tech_level") { +skill.tl }
                        xmlTag("difficulty") { +skill.difficulty }
                        xmlTag("points") { +skill.points }
                        xmlTag("reference") { +skill.reference }
                        skill.categories.forEach {
                            xmlTag("categories") {
                                xmlTag("category") { +it }
                            }
                        }
                        skill.prereqList.forEach {prereqList ->
                            xmlAttrTag("prereq_list", Pair("all", if (prereqList.all)"yes" else "no")) {
                                prereqList.skillPrereqList.forEach { skillPrereq ->
                                    xmlAttrTag("skill_prereq", Pair("has", if (skillPrereq.has)"yes" else "no")) {
                                        xmlAttrTag("name", Pair("compare", skillPrereq.nameCompare)) { +skillPrereq.name }
                                        xmlAttrTag("specialization", Pair("compare", skillPrereq.specializationCompare)) { +skillPrereq.specialization }
                                        xmlAttrTag("level", Pair("compare", skillPrereq.levelCompare)) { +skillPrereq.level }
                                    }
                                }
                                prereqList.advantagePrereqList.forEach { advPrereq ->
                                    xmlAttrTag("skill_prereq", Pair("has", if (advPrereq.has)"yes" else "no")) {
                                        xmlAttrTag("name", Pair("compare", advPrereq.nameCompare)) { +advPrereq.name }
                                        xmlAttrTag("notes", Pair("compare", advPrereq.notesCompare)) { +advPrereq.notes }
                                    }
                                }
                            }
                        }
                        skill.defaults.forEach { default ->
                            xmlTag("default") {
                                xmlTag("type") { +default.type }
                                xmlTag("name") { +default.name }
                                xmlTag("specialization") { +default.specialization }
                                xmlTag("modifier") { +default.modifier }
                            }
                        }
                    }
                }
            }
        }

    fun saveInFile(fileName: String, characterXML: String) {
        val fos = FileOutputStream("${Environment.getExternalStorageDirectory().path}/$fileName.gcs")
        fos.write(characterXML.toByteArray(Charset.forName("windows-1251")))
        fos.close()
    }
}