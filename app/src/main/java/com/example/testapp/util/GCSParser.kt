package com.example.testapp.util

import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.example.testapp.db.entity.Character
import com.example.testapp.db.entity.Default
import com.example.testapp.db.entity.Skill
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


class GCSParser {

    private val TAG = "GCS_PARSER"

    fun parseGCStoLog(fileName: String) {
        try {
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            val file =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/$fileName.gcs")
            val fis = FileInputStream(file)
            parser.setInput(InputStreamReader(fis))

            //val parser: XmlPullParser = context.resources.getXml(R.xml.contacts)
            while (parser.eventType != XmlPullParser.END_DOCUMENT) {
                var tmp = ""
                when (parser.eventType) {
                    XmlPullParser.START_DOCUMENT -> Log.d(TAG, "Начало документа")
                    XmlPullParser.START_TAG -> {
                        Log.d(
                            TAG,
                            "START_TAG: имя тега = ${parser.name}, уровень = ${parser.depth}, число атрибутов = ${parser.attributeCount}"
                        )
                        tmp = ""
                        var i = 0
                        while (i < parser.attributeCount) {
                            tmp =
                                ("$tmp${parser.getAttributeName(i)} = ${parser.getAttributeValue(i)}, ")
                            i++
                        }
                        if (!TextUtils.isEmpty(tmp)) Log.d(TAG, "Атрибуты: $tmp")
                    }
                    XmlPullParser.END_TAG -> Log.d(TAG, "END_TAG: имя тега = ${parser.name}")
                    XmlPullParser.TEXT -> Log.d(TAG, "текст = ${parser.text}")
                    else -> {
                    }
                }
                parser.next()
            }
        } catch (t: Throwable) {
            Log.d(TAG, "Ошибка при загрузке XML-документа: $t")
        }
    }

    //fun parseGCStoData(fileName: String?): Character = parseGCStoData(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/$fileName.gcs")

    fun parseGCStoData(filePath: String?): Character {
        val character = Character()
        try {
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            val file = File(filePath.toString())
            val fis = FileInputStream(file)
            parser.setInput(fis, "windows-1251")
//            parser.setInput(InputStreamReader(fis, Charsets.UTF_8))

            //val parser: XmlPullParser = context.resources.getXml(R.xml.contacts)
            while (parser.eventType != XmlPullParser.END_DOCUMENT) {
                when (parser.eventType) {
                    XmlPullParser.START_DOCUMENT -> Log.d(TAG, "Начало документа")
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "profile" -> parseProfile(parser, character)
//                            "advantage_list" -> TODO
                            "skill_list" -> parseSkillList(parser, character)
//                            "spell_list" -> TODO
//                            "equipment_list" -> TODO
//                            "notes" -> TODO
//                            "history" -> TODO
                            "HP" -> {
                                parser.next()
                                character.hp = parser.text.toInt()
                            }
                            "FP" -> {
                                parser.next()
                                character.fp = parser.text.toInt()
                            }
                            "total_points" -> {
                                parser.next()
                                character.totalPoints = parser.text.toInt()
                            }
                            "earn_points" -> {
                                parser.next()
                                character.earnPoints = parser.text.toInt()
                            }
                            "disadvantages_threshold" -> {
                                parser.next()
                                character.disadvPoints = parser.text.toInt()
                            }
                            "quirks_threshold" -> {
                                parser.next()
                                character.quirksPoints = parser.text.toInt()
                            }
                            "ST" -> {
                                parser.next()
                                character.st = parser.text.toInt()
                            }
                            "DX" -> {
                                parser.next()
                                character.dx = parser.text.toInt()
                            }
                            "IQ" -> {
                                parser.next()
                                character.iq = parser.text.toInt()
                            }
                            "HT" -> {
                                parser.next()
                                character.ht = parser.text.toInt()
                            }
                            "will" -> {
                                parser.next()
                                character.will = parser.text.toInt()
                            }
                            "perception" -> {
                                parser.next()
                                character.per = parser.text.toInt()
                            }
                            "speed" -> {
                                parser.next()
                                character.speed = parser.text.toInt()
                            }
                            "move" -> {
                                parser.next()
                                character.move = parser.text.toInt()
                            }
                            "hp_lost" -> {
                                character.wounds = parser.getAttributeValue(0)
                            }
                            "fp_lost" -> {
                                character.fpLoss = parser.getAttributeValue(0)
                            }
                        }
                    }
                    XmlPullParser.END_DOCUMENT -> Log.d(TAG, "Конец документа")
                }
                parser.next()
            }
        } catch (t: Throwable) {
            Log.d(TAG, "Ошибка при загрузке XML-документа: $t")
        }
        return character
    }

    private fun parseSkillList(parser: XmlPullParser, character: Character) {
        var skillContainer = "empty"
        val skillsList = mutableListOf<Skill>()
        parser.next()
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> when (parser.name) {
                    "skill_container" -> {
                        parser.next()
                        parser.next()
                        skillContainer = parser.text ?: "empty"
                    }
                    "skill" -> {

                        skillsList.add(parseSkill(parser, skillContainer))
                    }
                }
                XmlPullParser.END_TAG -> when(parser.name) {
                    "skill_list" -> {
                        character.skills = skillsList
                        return
                    }
                    "skill_container" -> skillContainer = "empty"
                }
            }
            parser.next()
        }
    }

    private fun parseSkill(parser: XmlPullParser, container: String): Skill {
        val parsedDefaults = mutableListOf<Default>()
        val parsedSkill = Skill()//(container = container) todo
        var currentTag = ""
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    when(currentTag) {
                        "categories" -> parseSkillCategories(parser, parsedSkill)
                        "default" -> parseSkillDefault(parser, parsedDefaults)
                    }
                }
                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "name" -> {parsedSkill.name = parser.text ?: ""}
                        "name-loc" -> {parsedSkill.name_loc = parser.text ?: ""}
                        "description-loc" -> {parsedSkill.description_loc = parser.text ?: ""}
                        "tech_level" -> {parsedSkill.tl = parser.text ?: ""}
                        "difficulty" -> {parsedSkill.difficulty = parser.text ?: ""}
                        "specialization" -> {parsedSkill.specialization = parser.text ?: ""}
                        "points" -> {parsedSkill.points = parser.text ?: ""}
                        "reference" -> {parsedSkill.reference = parser.text ?: ""}
                        "parry" -> {parsedSkill.parry = parser.text ?: ""}
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "skill") return parsedSkill else currentTag = ""
            }
            parser.next()
        }
    }

    private fun parseSkillDefault(parser: XmlPullParser, defaultList: MutableList<Default>) {
        val parsedDefault = Default()
        var currentTag = ""
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> currentTag = parser.name
                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "type" -> {parsedDefault.type = parser.text ?: ""}
                        "name" -> {parsedDefault.name = parser.text ?: ""}
                        "modifier" -> {parsedDefault.modifier = parser.text ?: ""}
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "default") {
                    defaultList.add(parsedDefault)
                    return
                } else currentTag = ""
            }
            parser.next()
        }
    }

    private fun parseSkillCategories(parser: XmlPullParser, parsedSkill: Skill) {
        val parsedCategories = mutableListOf<String>()
        var currentTag = ""
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> currentTag = parser.name
                XmlPullParser.TEXT -> {
                    if (currentTag == "category") {
                         parsedCategories.add(parser.text ?: "")
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "categories") {
                    parsedSkill.categories = parsedCategories
                    return
                } else currentTag = ""
            }
            parser.next()
        }
    }

    private fun parseProfile(parser: XmlPullParser, character: Character) {
        var currentTag = ""
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> currentTag = parser.name
                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "name" -> { character.name = parser.text ?: ""}
                        "player_name" -> {character.playerName = parser.text ?: ""}
                        "campaign" -> {character.world = parser.text ?: ""}
                        "tech_level" -> {character.tl = parser.text ?: ""}
                        //"title" -> TODO
                        "age" -> {character.age = parser.text ?: ""}
                        //"birthday" -> TODO
                        "eyes" -> { character.eyes = parser.text ?: ""}
                        "hair" -> { character.hairs = parser.text ?: ""}
                        "skin" -> { character.skin = parser.text ?: ""}
                        "handedness" -> { character.mainHand = parser.text ?: ""}
                        "height" -> { character.height = parser.text ?: ""}
                        "weight" -> { character.weight = parser.text ?: ""}
                        "gender" -> { character.gender = parser.text ?: ""}
                        "race" -> { character.race = parser.text ?: ""}
//                        "religion" -> TODO
                        "sm" -> { character.sm = parser.text ?: ""}
                        "notes" -> { character.description = parser.text ?: ""}
                        "portrait" -> { character.portrait = parser.text ?: "" }
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "profile") return else currentTag = ""
            }
            parser.next()
        }
    }
}
