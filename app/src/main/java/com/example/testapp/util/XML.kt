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
