package org.dicio.numbers.lang.es

import org.dicio.numbers.formatter.Formatter
import org.dicio.numbers.unit.MixedFraction
import org.dicio.numbers.util.Utils
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

class SpanishFormatter : Formatter("config/es-ES") {

    override fun niceNumber(mixedFraction: MixedFraction, speech: Boolean): String {
        if (speech) {
            val sign = if (mixedFraction.negative) "menos " else ""
            if (mixedFraction.numerator == 0) {
                return sign + pronounceNumber(mixedFraction.whole.toDouble(), 0, true, false, false)
            }

            var denominatorString = if (mixedFraction.denominator == 2) {
                "medio"
            } else {
                pronounceNumber(mixedFraction.denominator.toDouble(), 0, true, false, true)
            }

            val numeratorString = if (mixedFraction.numerator == 1) {
                "un"
            } else {
                if (denominatorString.endsWith("o")) {
                    denominatorString = denominatorString.substring(0, denominatorString.length - 1) + "os"
                } else if (denominatorString.endsWith("e")) {
                    denominatorString = denominatorString.substring(0, denominatorString.length - 1) + "es"
                } else {
                    denominatorString += "s"
                }
                pronounceNumber(mixedFraction.numerator.toDouble(), 0, true, false, false)
            }

            return if (mixedFraction.whole == 0L) {
                "$sign$numeratorString $denominatorString"
            } else {
                (sign + pronounceNumber(mixedFraction.whole.toDouble(), 0, true, false, false)
                        + " y " + numeratorString + " " + denominatorString)
            }
        } else {
            return niceNumberNotSpeech(mixedFraction)
        }
    }

    override fun pronounceNumber(
        number: Double,
        places: Int,
        shortScale: Boolean,
        scientific: Boolean,
        ordinal: Boolean
    ): String {
        if (number == Double.POSITIVE_INFINITY) {
            return "infinito"
        } else if (number == Double.NEGATIVE_INFINITY) {
            return "menos infinito"
        } else if (java.lang.Double.isNaN(number)) {
            return "no es un número"
        }

        if (scientific || abs(number) > 999999999999999934463.0) {
            val scientificFormatted = String.format(Locale.ENGLISH, "%E", number)
            val parts = scientificFormatted.split("E".toRegex(), limit = 2).toTypedArray()
            val power = parts[1].toInt().toDouble()

            if (power != 0.0) {
                val n = parts[0].toDouble()
                return String.format(
                    "%s por diez elevado a %s",
                    pronounceNumber(n, places, shortScale, false, false),
                    pronounceNumber(power, places, shortScale, false, false)
                )
            }
        }

        val result = StringBuilder()
        var varNumber = number
        if (varNumber < 0) {
            varNumber = -varNumber
            if (places != 0 || varNumber >= 0.5) {
                result.append("menos ")
            }
        }

        val realPlaces = Utils.decimalPlacesNoFinalZeros(varNumber, places)
        val numberIsWhole = realPlaces == 0
        val realOrdinal = ordinal && numberIsWhole
        val numberLong = varNumber.toLong() + (if (varNumber % 1 >= 0.5 && numberIsWhole) 1 else 0)

        if (realOrdinal && ORDINAL_NAMES.containsKey(numberLong)) {
            result.append(ORDINAL_NAMES[numberLong])
        } else if (!realOrdinal && NUMBER_NAMES.containsKey(numberLong)) {
            if (varNumber > 1000) {
                result.append("un ")
            }
            result.append(NUMBER_NAMES[numberLong])
        } else {
            val groups = Utils.splitByModulus(numberLong, 1000)
            val groupNames: MutableList<String> = ArrayList()
            for (i in groups.indices) {
                val z = groups[i]
                if (z == 0L) {
                    continue
                }
                var groupName = subThousand(z)

                if (i == 1) {
                    if (z == 1L) {
                        groupName = "mil"
                    } else {
                        groupName += " mil"
                    }
                } else if (i != 0) {
                    if (z == 1L) {
                        groupName = "un"
                    }

                    val magnitude = Utils.longPow(1000, i)
                    groupName += " " + NUMBER_NAMES[magnitude]
                    if (z != 1L) {
                        if (groupName.endsWith("ón")) {
                            groupName = groupName.substring(0, groupName.length - 2) + "ones"
                        } else if (groupName.endsWith("ardo")) {
                            groupName = groupName.substring(0, groupName.length - 1) + "os"
                        } else {
                            groupName += "s"
                        }
                    }
                }

                groupNames.add(groupName)
            }

            appendSplitGroups(result, groupNames)

            if (ordinal && numberIsWhole) {
                // Ordinalización básica (se puede mejorar)
                if (result.endsWith(" diez")) {
                    result.deleteRange(result.length - 5, result.length)
                    result.append("décimo")
                } else {
                    result.append("avo")
                }
            }
        }

        if (realPlaces > 0) {
            if (varNumber < 1.0 && (result.isEmpty() || "menos ".contentEquals(result))) {
                result.append("cero")
            }
            result.append(" coma")

            val fractionalPart = String.format("%." + realPlaces + "f", varNumber % 1)
            for (i in 2 until fractionalPart.length) {
                result.append(" ")
                result.append(NUMBER_NAMES[(fractionalPart[i].code - '0'.code).toLong()])
            }
        }

        return result.toString()
    }

    override fun niceTime(
        time: LocalTime,
        speech: Boolean,
        use24Hour: Boolean,
        showAmPm: Boolean
    ): String {
        if (speech) {
            val result = StringBuilder()
            if (time.minute == 45) {
                when (val newHour = (time.hour + 1) % 24) {
                    0 -> result.append("un cuarto para la medianoche")
                    12 -> result.append("un cuarto para el mediodía")
                    else -> {
                        result.append("un cuarto para las ")
                        result.append(getHourName(newHour, use24Hour))
                    }
                }
            } else {
                result.append(getHourName(time.hour, use24Hour))

                when (time.minute) {
                    0 -> result.append(" en punto")
                    15 -> result.append(" y cuarto")
                    30 -> result.append(" y media")
                    else -> {
                        result.append(" y ")
                        if (time.minute < 10) {
                            result.append("cero ")
                        }
                        result.append(pronounceNumberDuration(time.minute.toLong()))
                    }
                }
            }

            if (!use24Hour && showAmPm && result.indexOf("medianoche") == -1 && result.indexOf("mediodía") == -1) {
                if (time.hour >= 19) {
                    result.append(" de la noche")
                } else if (time.hour >= 12) {
                    result.append(" de la tarde")
                } else if (time.hour >= 4) {
                    result.append(" de la mañana")
                } else {
                    result.append(" de la madrugada")
                }
            }
            return result.toString()
        } else {
            if (use24Hour) {
                return time.format(DateTimeFormatter.ofPattern("HH:mm", Locale("es")))
            } else {
                val result = time.format(
                    DateTimeFormatter.ofPattern(
                        if (showAmPm) "K:mm a" else "K:mm", Locale.ENGLISH
                    )
                )
                return if (result.startsWith("0:")) {
                    "12:" + result.substring(2)
                } else {
                    result
                }
            }
        }
    }

    private fun getHourName(hour: Int, use24Hour: Boolean): String {
        if (hour == 0) {
            return "medianoche"
        } else if (hour == 12) {
            return "mediodía"
        }
        val normalizedHour = if (use24Hour) {
            hour
        } else {
            hour % 12
        }

        return if (normalizedHour == 1) {
            "la una"
        } else {
            "las " + pronounceNumberDuration(normalizedHour.toLong())
        }
    }

    override fun pronounceNumberDuration(number: Long): String {
        if (number == 1L) {
            return "una"
        }
        return super.pronounceNumberDuration(number)
    }

    private fun subThousand(n: Long): String {
        if (n == 0L) {
            return "cero"
        }
        val builder = StringBuilder()
        var requiresSpace = false
        if (n >= 100) {
            val hundred = n / 100
            when (hundred) {
                1L -> if (n % 100 == 0L) {
                    builder.append("cien")
                    requiresSpace = false
                } else {
                    builder.append("ciento")
                    requiresSpace = true
                }
                5L -> {
                    builder.append("quinientos")
                    requiresSpace = true
                }
                7L -> {
                    builder.append("setecientos")
                    requiresSpace = true
                }
                9L -> {
                    builder.append("novecientos")
                    requiresSpace = true
                }
                else -> {
                    builder.append(NUMBER_NAMES[hundred])
                    builder.append("cientos")
                    requiresSpace = true
                }
            }
        }

        val lastTwoDigits = n % 100
        if (lastTwoDigits != 0L) {
            if (requiresSpace) {
                builder.append(" ")
            }
            if (NUMBER_NAMES.containsKey(lastTwoDigits)) {
                builder.append(NUMBER_NAMES[lastTwoDigits])
            } else {
                val ten = (lastTwoDigits / 10) * 10
                val unit = lastTwoDigits % 10
                if (ten != 0L) {
                    builder.append(NUMBER_NAMES[ten])
                    if (unit != 0L) {
                        builder.append(" y ")
                        builder.append(NUMBER_NAMES[unit])
                    }
                } else {
                    builder.append(NUMBER_NAMES[unit])
                }
            }
        }

        return builder.toString()
    }

    private fun appendSplitGroups(result: StringBuilder, groupNames: List<String>) {
        if (groupNames.isNotEmpty()) {
            result.append(groupNames[groupNames.size - 1])
        }

        for (i in groupNames.size - 2 downTo 0) {
            result.append(", ")
            result.append(groupNames[i])
        }
    }

    companion object {
        private val NUMBER_NAMES = mapOf(
            0L to "cero",
            1L to "uno",
            2L to "dos",
            3L to "tres",
            4L to "cuatro",
            5L to "cinco",
            6L to "seis",
            7L to "siete",
            8L to "ocho",
            9L to "nueve",
            10L to "diez",
            11L to "once",
            12L to "doce",
            13L to "trece",
            14L to "catorce",
            15L to "quince",
            16L to "dieciséis",
            17L to "diecisiete",
            18L to "dieciocho",
            19L to "diecinueve",
            20L to "veinte",
            21L to "veintiuno",
            22L to "veintidós",
            23L to "veintitrés",
            24L to "veinticuatro",
            25L to "veinticinco",
            26L to "veintiséis",
            27L to "veintisiete",
            28L to "veintiocho",
            29L to "veintinueve",
            30L to "treinta",
            31L to "treinta y uno",
            40L to "cuarenta",
            50L to "cincuenta",
            60L to "sesenta",
            70L to "setenta",
            80L to "ochenta",
            90L to "noventa",
            100L to "cien",
            200L to "doscientos",
            300L to "trescientos",
            400L to "cuatrocientos",
            500L to "quinientos",
            600L to "seiscientos",
            700L to "setecientos",
            800L to "ochocientos",
            900L to "novecientos",
            1000L to "mil",
            1000000L to "millón",
            1000000000L to "mil millones",
            1000000000000L to "billón",
            1000000000000000L to "mil billones",
            1000000000000000000L to "trillón"
        )

        private val ORDINAL_NAMES = mapOf(
            1L to "primero",
            2L to "segundo",
            3L to "tercero",
            4L to "cuarto",
            5L to "quinto",
            6L to "sexto",
            7L to "séptimo",
            8L to "octavo",
            9L to "noveno",
            10L to "décimo",
            11L to "undécimo",
            12L to "duodécimo",
            13L to "decimotercero",
            14L to "decimocuarto",
            15L to "decimoquinto",
            16L to "decimosexto",
            17L to "decimoséptimo",
            18L to "decimoctavo",
            19L to "decimonoveno",
            20L to "vigésimo",
            30L to "trigésimo",
            40L to "cuadragésimo",
            50L to "quincuagésimo",
            60L to "sexagésimo",
            70L to "septuagésimo",
            80L to "octogésimo",
            90L to "nonagésimo",
            100L to "centésimo",
            1000L to "milésimo",
            1000000L to "millonésimo",
            1000000000L to "milmillonésimo",
            1000000000000L to "billonésimo"
        )
    }
}
