package com.example.testapp.util

import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.example.testapp.db.entity.*
import com.example.testapp.db.entity.advantage.*
import com.example.testapp.db.entity.Skill.Default
import com.example.testapp.db.entity.Skill.PrereqList
import com.example.testapp.db.entity.Skill.Skill
import com.example.testapp.db.entity.Skill.SkillPrereq
import com.example.testapp.di.DBModelImpl
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.inject.Inject


class GCSParser @Inject constructor(){

    private val TAG = "GCS_PARSER"

    private val dbm: DBModelImpl by inject()

    var filePath: String = ""

    var character: Character = Character()
    private set

    init {
        val scope = Toothpick.openScope("APP")
        scope.inject(this)
    }

    fun parse(characterId: Int){
        if (filePath.isNotBlank()){
            character = Character()
            character.id = characterId
            parseGCStoData()
        }
    }

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
                        Log.d(TAG, "START_TAG: имя тега = ${parser.name}, уровень = ${parser.depth}, число атрибутов = ${parser.attributeCount}")
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

    private fun parseGCStoData(){
        try {
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            val file = File(filePath)
            val fis = FileInputStream(file)
            parser.setInput(fis, "windows-1251")
            while (parser.eventType != XmlPullParser.END_DOCUMENT) {
                when (parser.eventType) {
                    XmlPullParser.START_DOCUMENT -> Log.d(TAG, "Начало документа")
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "character" -> {
                                character.version = parser.getAttributeValue(0)
                                if (parser.attributeCount > 1) character.measure = parser.getAttributeValue(1)
                            }
                            "created_date" -> {
                                parser.next()
                                character.createdDate = parser.text
                            }
                            "modified_date" -> {
                                parser.next()
                                character.modifiedDate = parser.text
                            }
                            "profile" -> parseProfile(parser)
                            "advantage_list" -> {
                                character.skillListSize = parser.getAttributeValue(0)
                                val advList = parseAdvantageList(parser)
                                print(advList.toString())
                            }
                            "skill_list" -> {
                                character.skillListSize = parser.getAttributeValue(0)
                                dbm.saveCharacterSkills(parseSkillList(parser), character.id)
                            }
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
                                character.speed = parser.text.toFloat()
                            }
                            "move" -> {
                                parser.next()
                                character.move = parser.text.toInt()
                            }
                            "hp_lost" -> {
                                character.hpLossTotal = parser.getAttributeValue(0)
                                parser.next()
                                character.hpLoss = parser.text ?: ""
                            }
                            "fp_lost" -> {
                                character.fpLossTotal = parser.getAttributeValue(0)
                                parser.next()
                                character.fpLoss = parser.text ?: ""
                            }
                        }
                    }
                    XmlPullParser.END_DOCUMENT -> Log.d(TAG, "Конец документа")
                }
                parser.next()
            }
            fis.channel.close()
        } catch (t: Throwable) {
            Log.d(TAG, "Ошибка при загрузке XML-документа: ${t.printStackTrace()}")
        }
    }

    fun parseSkillList(parser: XmlPullParser): MutableList<Skill> {
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
                        return skillsList
                    }
                    "skill_container" -> skillContainer = "empty"
                }
            }
            parser.next()
        }
    }

    private fun parseSkill(parser: XmlPullParser, container: String): Skill {
        val parsedDefaults = mutableListOf<Default>()
        val parsedPrereqList = mutableListOf<PrereqList>()
        var prereqParentCounter = 0
        val parsedSkill = Skill(
            container = container,
            version = parser.getAttributeValue(0)
        )
        var currentTag = ""
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    when(currentTag) {
                        "categories" -> parsedSkill.categories = parseCategories(parser)
                        "default" -> parseSkillDefault(parser, parsedDefaults)
                        "prereq_list" -> {
                            parsePrereqList(parser, parsedPrereqList, 0, prereqParentCounter)
                            prereqParentCounter++
                        }
                    }
                }
                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "name" -> {parsedSkill.name = parser.text ?: ""}
                        "name-loc" -> {parsedSkill.nameLoc = parser.text ?: ""}
                        "description-loc" -> {parsedSkill.descriptionLoc = parser.text ?: ""}
                        "tech_level" -> {parsedSkill.tl = parser.text ?: ""}
                        "difficulty" -> {parsedSkill.difficulty = parser.text ?: ""}
                        "specialization" -> {parsedSkill.specialization = parser.text ?: ""}
                        "points" -> {parsedSkill.points = parser.text ?: ""}
                        "reference" -> {parsedSkill.reference = parser.text ?: ""}
                        "parry" -> {parsedSkill.parry = parser.text ?: ""}
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "skill") {
                    parsedSkill.defaults = parsedDefaults
                    parsedSkill.prereqList = parsedPrereqList
                    return parsedSkill
                } else {
                    currentTag = ""
                }
            }
            parser.next()
        }
    }

    private fun parsePrereqList(parser: XmlPullParser, parsedPrereqListList: MutableList<PrereqList>, depth: Int, parent: Int) {
        var parsedPrereqList = PrereqList(
            all = parser.getAttributeValue(0) == "yes",
            depth = depth,
            parent = parent
        )
        val parsedSkillPrereq = mutableListOf<SkillPrereq>()
        val parsedAdvPrereq = mutableListOf<AdvantagePrereq>()
//        val parsedAttrPrereq = mutableListOf<AttributePrereq>()//todo
        parser.next()
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    when(parser.name) {
                        "skill_prereq" -> parsedSkillPrereq.add(parseSkillPrereq(parser))
                        "advantage_prereq" -> parsedAdvPrereq.add(parseAdvantagePrereq(parser))
                        //"attribute_prereq" -> parsedAttrPrereq.add(parseAttrPrereq(parser))
                        "prereq_list" -> parsePrereqList(parser, parsedPrereqListList, parsedPrereqList.depth+1, parsedPrereqList.parent+1)
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "prereq_list") {
                    parsedPrereqList.skillPrereqList = parsedSkillPrereq
                    parsedPrereqListList.add(parsedPrereqList)
                    return
                }
            }
            parser.next()
        }
    }

    private fun parseSkillPrereq(parser: XmlPullParser): SkillPrereq {
        val parsedSkillPrereq = SkillPrereq()
        var currentTag = ""
        //parser.next()
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    when(currentTag) {
                        "skill_prereq" -> parsedSkillPrereq.has = parser.getAttributeValue(0) == "yes"
                        "name" -> parsedSkillPrereq.nameCompare = parser.getAttributeValue(0)
                        "specialization" -> parsedSkillPrereq.specializationCompare = parser.getAttributeValue(0)
                        "level" -> parsedSkillPrereq.levelCompare = parser.getAttributeValue(0)
                    }
                }
                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "name" -> {parsedSkillPrereq.name = parser.text ?: ""}
                        "level" -> {parsedSkillPrereq.level = parser.text ?: ""}
                        "specialization" -> {parsedSkillPrereq.specialization = parser.text ?: ""}
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "skill_prereq") return parsedSkillPrereq else currentTag = ""
            }
            parser.next()
        }
    }

    private fun parseAdvantagePrereq(parser: XmlPullParser): AdvantagePrereq {
        val parsedSkillPrereq = AdvantagePrereq()
        var currentTag = ""
        //parser.next()
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    when(currentTag) {
                        "advantage_prereq" -> parsedSkillPrereq.has = parser.getAttributeValue(0) == "yes"
                        "name" -> parsedSkillPrereq.nameCompare = parser.getAttributeValue(0)
                        "notes" -> parsedSkillPrereq.notesCompare = parser.getAttributeValue(0)
                    }
                }
                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "name" -> {parsedSkillPrereq.name = parser.text ?: ""}
                        "notes" -> {parsedSkillPrereq.notes = parser.text ?: ""}
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "advantage_prereq") return parsedSkillPrereq else currentTag = ""
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
                        "specialization" -> {parsedDefault.specialization = parser.text ?: ""}
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

    private fun parseCategories(parser: XmlPullParser) : List<String> {
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
                    return parsedCategories
                } else currentTag = ""
            }
            parser.next()
        }
    }

    fun parseAdvantageList(parser: XmlPullParser): MutableList<Advantage> {
        var advContainer = "empty"
        val advList = mutableListOf<Advantage>()
        parser.next()
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> when (parser.name) {
                    "advantage_container" -> {
                        parser.next()
                        parser.next()
                        advContainer = parser.text ?: "empty"
                    }
                    "advantage" -> {
                        advList.add(parseAdvantage(parser, advContainer))
                    }
                }
                XmlPullParser.END_TAG -> when(parser.name) {
                    "advantage_list" -> {
                        return advList
                    }
                    "advantage_container" -> advContainer = "empty"
                }
            }
            parser.next()
        }
    }

    private fun parseAdvantage(parser: XmlPullParser, container: String): Advantage {
        val parsedModifiers = mutableListOf<Modifier>()
        val parsedSkillBonuses = mutableListOf<SkillBonus>()
        val parsedAttributeBonuses = mutableListOf<AttributeBonus>()
        val parsedPrereqList = mutableListOf<PrereqList>()
        var prereqParentCounter = 0
        val parsedAdvantage = Advantage(
            container = container,
            version = parser.getAttributeValue(0)
        )
        var currentTag = ""
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    when(currentTag) {
                        "modifier" -> parseAdvantageModifier(parser, parsedModifiers)
                        "categories" -> parsedAdvantage.categories = parseCategories(parser)
                        "skill_bonus" -> parseAdvantageSkillBonus(parser, parsedSkillBonuses)
                        "attribute_bonus" -> parseAdvantageAttributeBonus(parser, parsedAttributeBonuses)
                        "prereq_list" -> {
                            parsePrereqList(parser, parsedPrereqList, 0, prereqParentCounter)
                            prereqParentCounter++
                        }
                    }
                }
                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "name" -> {parsedAdvantage.name = parser.text ?: ""}
                        "name-loc" -> {parsedAdvantage.nameLoc = parser.text ?: ""}
                        "description-loc" -> {parsedAdvantage.descriptionLoc = parser.text ?: ""}
                        "type" -> {parsedAdvantage.type = parser.text ?: ""}
                        "levels" -> {parsedAdvantage.levels = parser.text ?: ""}
                        "points_per_level" -> {parsedAdvantage.pointsPerLevel = parser.text ?: ""}
                        "base_points" -> {parsedAdvantage.basePoints = parser.text ?: ""}
                        "reference" -> {parsedAdvantage.reference = parser.text ?: ""}
                        "notes" -> {parsedAdvantage.notes = parser.text ?: ""}
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "advantage") {
                    parsedAdvantage.modifiers = parsedModifiers
                    parsedAdvantage.skillBonuses = parsedSkillBonuses
                    parsedAdvantage.attributeBonuses = parsedAttributeBonuses
                    parsedAdvantage.prereqList = parsedPrereqList
                    return parsedAdvantage
                } else {
                    currentTag = ""
                }
            }
            parser.next()
        }
    }

    private fun parseAdvantageModifier(parser: XmlPullParser, modifierList: MutableList<Modifier>) {
        val parsedModifier = Modifier(
            version = parser.getAttributeValue(0),
            enabled = parser.getAttributeValue(1) == "yes"
        )
        var currentTag = ""
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    when (currentTag) {
                        "cost" -> parsedModifier.costType = parser.getAttributeValue(0)
                    }
                }
                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "name" -> {parsedModifier.name = parser.text ?: ""}
                        "cost" -> {parsedModifier.cost = parser.text.toInt()}
                        "affects" -> {parsedModifier.affects = parser.text ?: ""}
                        "reference" -> {parsedModifier.reference = parser.text ?: ""}
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "modifier") {
                    modifierList.add(parsedModifier)
                    return
                } else currentTag = ""
            }
            parser.next()
        }
    }

    private fun parseAdvantageSkillBonus (parser: XmlPullParser, skillBonusList: MutableList<SkillBonus>) {
        val parsedSkillBonus = SkillBonus()
        var currentTag = ""
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    when (currentTag) {
                        "name" -> parsedSkillBonus.nameCompare = parser.getAttributeValue(0)
                        "specialization" -> parsedSkillBonus.specializationCompare= parser.getAttributeValue(0)
                    }
                }
                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "name" -> {parsedSkillBonus.name = parser.text ?: ""}
                        "specialization" -> {parsedSkillBonus.specialization = parser.text ?: ""}
                        "amount" -> {parsedSkillBonus.amount = parser.text.toInt()}
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "skill_bonus") {
                    skillBonusList.add(parsedSkillBonus)
                    return
                } else currentTag = ""
            }
            parser.next()
        }
    }

    private fun parseAdvantageAttributeBonus (parser: XmlPullParser, skillBonusList: MutableList<AttributeBonus>) {
        val parsedAttributeBonus = AttributeBonus()
        var currentTag = ""
        while (true) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> currentTag = parser.name
                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "attribute" -> {parsedAttributeBonus.attribute = parser.text ?: ""}
                        "amount" -> {parsedAttributeBonus.amount = parser.text.toInt()}
                    }
                }
                XmlPullParser.END_TAG -> if (parser.name == "attribute_bonus") {
                    skillBonusList.add(parsedAttributeBonus)
                    return
                } else currentTag = ""
            }
            parser.next()
        }
    }

    private fun parseProfile(parser: XmlPullParser) {
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
                        "title" -> {character.state = parser.text ?: ""}
                        "age" -> {character.age = parser.text ?: ""}
                        "birthday" -> {character.birthday = parser.text ?: ""}
                        "eyes" -> { character.eyes = parser.text ?: ""}
                        "hair" -> { character.hairs = parser.text ?: ""}
                        "skin" -> { character.skin = parser.text ?: ""}
                        "handedness" -> { character.mainHand = parser.text ?: ""}
                        "height" -> { character.height = parser.text ?: ""}
                        "weight" -> { character.weight = parser.text ?: ""}
                        "gender" -> { character.gender = parser.text ?: ""}
                        "race" -> { character.race = parser.text ?: ""}
                        "religion" -> {character.religion = parser.text ?: ""}
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
