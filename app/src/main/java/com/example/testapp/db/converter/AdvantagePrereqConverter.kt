package com.example.testapp.db.converter

import androidx.room.TypeConverter
import com.example.testapp.db.entity.advantage.AdvantagePrereq

class AdvantagePrereqConverter {

    @TypeConverter
    fun fromList(list: List<AdvantagePrereq>): String = list.joinToString("*") { "${it.has}|${it.nameCompare}|${it.name}|${it.notesCompare}|${it.notes}"}

    @TypeConverter
    fun toList(data: String): List<AdvantagePrereq> = data.split("*").map { list ->
        if (!list.isBlank()) {
            var i = 0
            val advPrereq = AdvantagePrereq()
            list.split("|").forEach {
                when (i) {
                    0 -> advPrereq.has = it == "yes"
                    1 -> advPrereq.nameCompare = it
                    2 -> advPrereq.name = it
                    3 -> advPrereq.notesCompare = it
                    4 -> advPrereq.notes = it
                }
                i++
            }
            advPrereq
        } else AdvantagePrereq()
    }

}