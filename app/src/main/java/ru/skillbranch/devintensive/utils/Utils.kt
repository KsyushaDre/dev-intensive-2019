package ru.skillbranch.devintensive.utils

import java.lang.StringBuilder

object Utils {

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")

        val firstName: String?
        val lastName: String?

        val regex = Regex("\\s")

        if (fullName == null || fullName.isBlank()) {
            firstName = null
            lastName = null
        } else if (!regex.containsMatchIn(fullName)) {
            firstName = fullName
            lastName = null
        } else {
            firstName = parts?.getOrNull(0)
            lastName = parts?.getOrNull(1)
        }

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {

        fun string(): String = StringBuilder().apply {
            val newString = payload.toCharArray()
                .map { it.toString() }
                .map {
                    when {
                        it == "а" -> "a"
                        it == "б" -> "b"
                        it == "в" -> "v"
                        it == "г" -> "g"
                        it == "д" -> "d"
                        it == "е" -> "e"
                        it == "ё" -> "e"
                        it == "ж" -> "zh"
                        it == "з" -> "z"
                        it == "и" -> "i"
                        it == "й" -> "i"
                        it == "к" -> "k"
                        it == "л" -> "l"
                        it == "м" -> "m"
                        it == "н" -> "n"
                        it == "о" -> "o"
                        it == "п" -> "p"
                        it == "р" -> "r"
                        it == "с" -> "s"
                        it == "т" -> "t"
                        it == "у" -> "u"
                        it == "ф" -> "f"
                        it == "х" -> "h"
                        it == "ц" -> "c"
                        it == "ч" -> "ch"
                        it == "ш" -> "sh"
                        it == "щ" -> "sh"
                        it == "ъ" || it == "Ъ" -> ""
                        it == "ы" -> "i"
                        it == "ь" || it == "Ь" -> ""
                        it == "э" -> "e"
                        it == "ю" -> "yu"
                        it == "я" -> "ya"
                        it == "А" -> "A"
                        it == "Б" -> "B"
                        it == "В" -> "V"
                        it == "Г" -> "G"
                        it == "Д" -> "D"
                        it == "Е" -> "E"
                        it == "Ё" -> "E"
                        it == "Ж" -> "Zh"
                        it == "З" -> "Z"
                        it == "И" -> "I"
                        it == "Й" -> "I"
                        it == "К" -> "K"
                        it == "Л" -> "L"
                        it == "М" -> "M"
                        it == "Н" -> "N"
                        it == "О" -> "O"
                        it == "П" -> "P"
                        it == "Р" -> "R"
                        it == "С" -> "S"
                        it == "Т" -> "T"
                        it == "У" -> "U"
                        it == "Ф" -> "F"
                        it == "Х" -> "H"
                        it == "Ц" -> "C"
                        it == "Ч" -> "Ch"
                        it == "Ш" -> "Sh"
                        it == "Щ" -> "Sh"
                        it == "Ы" -> "I"
                        it == "Э" -> "E"
                        it == "Ю" -> "Yu"
                        it == "Я" -> "Ya"
                        it == " " -> divider
                        else -> it
                    }
                }.map { this.append(it) }
        }.toString()
        return string()
    }

    fun toInitials(firstName: String?, lastName: String?): String? {

        val regex = Regex("\\s")

        val firstInitial: String? = if (firstName == null || firstName.isBlank()) {
            null
        } else {
            firstName[0].toUpperCase().toString()
        }

        val secondInitial: String? = if (lastName == null || lastName.isBlank()) {
            null
        } else {
            lastName[0].toUpperCase().toString()
        }

        return if (firstInitial == null && secondInitial != null) {
            secondInitial
        } else if (firstInitial != null && secondInitial == null) {
            firstInitial
        } else if (firstInitial == null && secondInitial == null) {
            null
        } else {
            "$firstInitial$secondInitial"
        }
    }
}