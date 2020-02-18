package com.example.testapp.util

import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Skill.Skill
import java.io.FileOutputStream
import java.nio.charset.Charset

class GCSXmlBuilder {
    fun xmlTest(
        character: Character,
        skills: List<Skill>
    ) =
        startCharacterXML("2", "kg"){
            xmlTag("created_date") { +"13.03.2019 17:28" }
            xmlTag("modified_date") { +"16.03.2019 12:17" }
            xmlTag("profile") {
                xmlTag("name") { +character.name }
                xmlTag("player_name") { +character.playerName }
                xmlTag("campaign") { +character.world }
                xmlTag("tech_level") { +character.tl }
                xmlTag("title") { +"title?" }
                xmlTag("age") { + character.age }
                xmlTag("birthday") { +"birthday?" }
                xmlTag("eyes") { +character.eyes }
                xmlTag("hair") { +character.hairs }
                xmlTag("skin") { +character.skin }
                xmlTag("handedness") { +character.mainHand }
                xmlTag("height") { +character.height }
                xmlTag("weight") { +character.weight }
                xmlTag("gender") { +character.gender }
                xmlTag("race") { +character.race }
                xmlTag("religion") { +"religion?" }
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
            xmlAttrTag("hp_lost", Pair("total", "")) { +character.wounds }
            xmlAttrTag("fp_lost", Pair("total", "")) { +character.fpLoss }
            xmlAttrTag("skill_list", Pair("size", "-3")) {
                skills.forEach {
                    xmlAttrTag("skill", Pair("version", "2")) {
                        xmlTag("name") { +it.name }
                        xmlTag("name-loc") { +it.nameLoc }
                        xmlTag("description-loc") { +it.descriptionLoc }
                        xmlTag("tech_level") { +it.tl }
                        xmlTag("difficulty") { +it.difficulty }
                        xmlTag("points") { +it.points }
                        xmlTag("reference") { +it.reference }
                        it.categories.forEach {
                            xmlTag("categories") {
                                xmlTag("category") { +it }
                            }
                        }
                        it.prereqList.forEach {prereqList ->
                            xmlAttrTag("prereq_list", Pair("all", if (prereqList.all)"yes" else "no")) {
                                prereqList.skillPrereqList.forEach { skillPrereq ->
                                    xmlAttrTag("skill_prereq", Pair("has", if (skillPrereq.has)"yes" else "no")) {
                                        xmlAttrTag("name", Pair("compare", skillPrereq.nameCompare)) { +skillPrereq.name }
                                        xmlAttrTag("specialization", Pair("compare", skillPrereq.specializationCompare)) { +skillPrereq.specialization }
                                        xmlAttrTag("level", Pair("compare", skillPrereq.levelCompare)) { +skillPrereq.level }
                                    }
                                }
                            }
                        }
                        it.defaults.forEach {
                            xmlTag("default") {
                                xmlTag("type") { +it.type }
                                xmlTag("name") { +it.name }
                                xmlTag("specialization") { +it.specialization }
                                xmlTag("modifier") { +it.modifier }
                            }
                        }
                    }
                }
            }
        }

    fun saveInFile(fileName: String, characterXML: String) {
        val fos = FileOutputStream("/sdcard/$fileName.gcs")
        fos.write(characterXML.toByteArray(Charset.forName("windows-1251")))
        fos.close()
    }
}