package com.example.testapp.util

import android.util.Xml
import com.example.testapp.db.entity.Character
import org.xmlpull.v1.XmlSerializer
import java.io.FileOutputStream
import java.io.StringWriter
import java.nio.charset.Charset

class XMLBuilder {
    val xmlCreator: XmlSerializer = Xml.newSerializer()

    fun xmlTest(character: Character) =
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
        }


    fun createCharacterXML(character: Character): String {
        val writer = StringWriter()
        xmlCreator.apply {
            setOutput(writer)
            startDocument("UTF-8", true)
            startTag("", "character")
            attribute("",  "version", "2")
            attribute("",  "measure", "kg")

                singleTag(this, "created_date", "13.03.2019 17:28")
                singleTag(this, "modified_date", "16.03.2019 12:17")

                startTag("", "profile")
                    singleTag(this, "name", character.name)
                    singleTag(this, "player_name", character.playerName)
                    singleTag(this, "campaign", character.world)
                    singleTag(this, "tech_level", character.tl)
                    singleTag(this, "title", "title?")
                    singleTag(this, "age", character.age)
                    singleTag(this, "birthday", "birthday?")
                    singleTag(this, "eyes", character.eyes)
                    singleTag(this, "hair", character.hairs)
                    singleTag(this, "skin", character.skin)
                    singleTag(this, "handedness", character.mainHand)
                    singleTag(this, "height", character.height)
                    singleTag(this, "weight", character.weight)
                    singleTag(this, "gender", character.gender)
                    singleTag(this, "race", character.race)
                    singleTag(this, "religion", "religion?")
                    singleTag(this, "sm", character.sm)
                    singleTag(this, "notes", character.description)
                    singleTag(this, "portrait", character.portrait)
                endTag("", "profile")

                singleTag(this, "HP", character.hp.toString() )
                singleTag(this, "FP", character.fp.toString() )
                singleTag(this, "total_points", character.totalPoints.toString() )
                singleTag(this, "disadvantages_threshold", character.disadvPoints.toString() )
                singleTag(this, "quirks_threshold", character.quirksPoints.toString() )
                singleTag(this, "ST", character.st.toString() )
                singleTag(this, "DX", character.dx.toString() )
                singleTag(this, "IQ", character.iq.toString() )
                singleTag(this, "HT", character.ht.toString() )
                singleTag(this, "will", character.will.toString() )
                singleTag(this, "perception", character.per.toString() )
                singleTag(this, "speed", character.speed.toString() )
                singleTag(this, "move", character.move.toString() )

                startTag("", "hp_lost")
                    attribute("", "total", character.wounds )
                endTag("", "hp_lost")
                startTag("", "fp_lost")
                    attribute("", "total", character.fpLoss )
                endTag("", "fp_lost")

            endTag("", "character")
            endDocument()
        }

        val outStr = writer.toString()
        return outStr
    }

    private fun singleTag(xml: XmlSerializer, tagName: String, text: String)
    {
        xml.apply {
            startTag("", tagName)
            text(text)
            endTag("", tagName)
        }
    }

    fun saveInFile(fileName: String, characterXML: String) {
        val fos = FileOutputStream("/sdcard/$fileName.gcs")
        fos.write(characterXML.toByteArray(Charset.forName("windows-1251")))
        fos.close()
    }
}