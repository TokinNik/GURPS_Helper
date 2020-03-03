package com.example.testapp.util

interface Element {
    fun render(builder: StringBuilder)
}

class TextElement(val text: String) : Element {
    override fun render(builder: StringBuilder) {
        builder.append(text)
    }
}

@DslMarker
annotation class HtmlTagMarker

@HtmlTagMarker
abstract class Tag(val name: String) : Element {
    val children = arrayListOf<Element>()
    val attributes = hashMapOf<String, String>()
    val xmlHeader = """<xml version="1.0" encoding="UTF8">"""

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }

    override fun render(builder: StringBuilder) {
        builder.append("<$name${renderAttributes()}>")
        for (c in children) {
            c.render(builder)
        }
        builder.append("</$name>\n")
    }

    private fun renderAttributes(): String {
        val builder = StringBuilder()
        for ((attr, value) in attributes) {
            builder.append(" $attr=\"$value\"")
        }
        return builder.toString()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("$xmlHeader\n")
        render(builder)
        return builder.toString()
    }
}

abstract class TagWithText(name: String) : Tag(name) {
    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }
}

open class XML(name: String) : TagWithText(name) {

    fun xmlTag(name: String, init: XML.() -> Unit) = initTag(XML(name), init)

    fun xmlAttrTag(name: String, attr: Pair<String, String>, init: AttrTag.() -> Unit): AttrTag {
        val tag = initTag(AttrTag(name, attr), init)
        tag.attrName = attr.first
        tag.attrValue = attr.second
        return tag
    }

    fun xmlAttr2Tag(name: String, attr1: Pair<String, String>, attr2: Pair<String, String>, init: Attr2Tag.() -> Unit): Attr2Tag {
        val tag = initTag(Attr2Tag(name, attr1, attr2), init)
        tag.attr1Name = attr1.first
        tag.attr1Value = attr1.second
        tag.attr2Name = attr2.first
        tag.attr2Value = attr2.second
        return tag
    }

    fun character(version: String, measure: String, init: CharacterTag.() -> Unit) {
        val ch = initTag(CharacterTag(), init)
        ch.version = version
        ch.measure = measure
    }
}

open class AttrTag(name: String, attr: Pair<String, String>) : TagWithText(name) {
    var attrName: String = attr.first
    var attrValue: String
        get() = attributes[attrName]!!
        set(value) {
            attributes[attrName] = value
        }

    fun xmlTag(name: String, init: XML.() -> Unit) = initTag(XML(name), init)

    fun xmlAttrTag(name: String, attr: Pair<String, String>, init: AttrTag.() -> Unit): AttrTag {
        val tag = initTag(AttrTag(name, attr), init)
        tag.attrName = attr.first
        tag.attrValue = attr.second
        return tag
    }

    fun xmlAttr2Tag(name: String, attr1: Pair<String, String>, attr2: Pair<String, String>, init: Attr2Tag.() -> Unit): Attr2Tag {
        val tag = initTag(Attr2Tag(name, attr1, attr2), init)
        tag.attr1Name = attr1.first
        tag.attr1Value = attr1.second
        tag.attr2Name = attr2.first
        tag.attr2Value = attr2.second
        return tag
    }
}

open class Attr2Tag(name: String, attr1: Pair<String, String>, attr2: Pair<String, String>) : TagWithText(name) {
    var attr1Name: String = attr1.first
    var attr1Value: String
        get() = attributes[attr1Name]!!
        set(value) {
            attributes[attr1Name] = value
        }
    var attr2Name: String = attr2.first
    var attr2Value: String
        get() = attributes[attr2Name]!!
        set(value) {
            attributes[attr2Name] = value
        }

    fun xmlTag(name: String, init: XML.() -> Unit) = initTag(XML(name), init)

    fun xmlAttrTag(name: String, attr: Pair<String, String>, init: AttrTag.() -> Unit): AttrTag {
        val tag = initTag(AttrTag(name, attr), init)
        tag.attrName = attr.first
        tag.attrValue = attr.second
        return tag
    }

    fun xmlAttr2Tag(name: String, attr1: Pair<String, String>, attr2: Pair<String, String>, init: Attr2Tag.() -> Unit): Attr2Tag {
        val tag = initTag(Attr2Tag(name, attr1, attr2), init)
        tag.attr1Name = attr1.first
        tag.attr1Value = attr1.second
        tag.attr2Name = attr2.first
        tag.attr2Value = attr2.second
        return tag
    }
}

class CharacterTag : XML("character") {
    var version: String
        get() = attributes["version"]!!
        set(value) {
            attributes["version"] = value
        }
    var measure: String
        get() = attributes["measure"]!!
        set(value) {
            attributes["measure"] = value
        }
}

fun startXML(name: String, init: XML.() -> Unit): XML {
    val xml = XML(name)
    xml.init()
    return xml
}

fun startCharacterXML(version: String, measure: String, init: XML.() -> Unit): XML {
    val ch = CharacterTag()
    ch.measure = measure
    ch.version = version
    ch.init()
    return ch
}
