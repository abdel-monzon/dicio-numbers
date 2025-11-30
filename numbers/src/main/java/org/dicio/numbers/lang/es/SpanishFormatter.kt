package org.dicio.numbers.lang.es

import org.dicio.numbers.formatter.Formatter
import org.dicio.numbers.unit.MixedFraction
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class SpanishFormatter : Formatter("config/es") {

    override fun niceNumber(mixedFraction: MixedFraction, speech: Boolean): String {
        return if (speech) {
            val sign = if (mixedFraction.negative) "menos " else ""
            if (mixedFraction.numerator == 0) {
                return sign + pronounceNumber(mixedFraction.whole.toDouble(), 0, true, false, false)
            }

            var denominatorString = when (mixedFraction.denominator) {
                2 -> "medio"
                4 -> "cuarto"
                else -> pronounceNumber(mixedFraction.denominator.toDouble(), 0, true, false, true)
            }

            val numeratorString: String = if (mixedFraction.numerator == 1) {
                "un"
            } else {
                pronounceNumber(mixedFraction.numerator.toDouble(), 0, true, false, false)
            }

            if (mixedFraction.whole == 0L) {
                "$sign$numeratorString $denominatorString"
            } else {
                (sign + pronounceNumber(mixedFraction.whole.toDouble(), 0, true, false, false)
                        + " y " + numeratorString + " " + denominatorString)
            }
        } else {
            niceNumberNotSpeech(mixedFraction)
        }
    }

    override fun pronounceNumber(
        number: Double,
        places: Int,
        shortScale: Boolean,
        scientific: Boolean,
        ordinal: Boolean
    ): String {
        if (number == Double.POSITIVE_INFINITY) return "infinito"
        if (number == Double.NEGATIVE_INFINITY) return "menos infinito"
        if (java.lang.Double.isNaN(number)) return "no es un número"

        if (scientific || abs(number) > 999999999999999934463.0) {
            val scientificFormatted = String.format(Locale.ENGLISH, "%E", number)
            val parts = scientificFormatted.split("E".toRegex(), limit = 2).toTypedArray()
            val power = parts[1].toInt().toDouble()
            if (power != 0.0) {
                val n = parts[0].toDouble()
                return String.format(
                    "%s%s por diez a la %s%s",
                    if (n < 0) "menos " else "",
                    pronounceNumber(abs(n), places, shortScale, false, false),
                    if (power < 0) "menos " else "",
                    pronounceNumber(abs(power), places, shortScale, false, false)
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

        val realPlaces = org.dicio.numbers.util.Utils.decimalPlacesNoFinalZeros(varNumber, places)
        val numberIsWhole = realPlaces == 0
        val numberLong = varNumber.toLong() + (if (varNumber % 1 >= 0.5 && numberIsWhole) 1 else 0)

        if (!ordinal && numberIsWhole && SPANISH_NUMBER_NAMES.containsKey(numberLong)) {
            result.append(SPANISH_NUMBER_NAMES[numberLong])
        } else {
            result.append(numberLong.toString())
        }

        if (realPlaces > 0) {
            if (varNumber < 1.0 && (result.isEmpty() || "menos " == result.toString())) {
                result.append("cero")
            }
            result.append(" coma")
            val fractionalPart = String.format("%." + realPlaces + "f", varNumber % 1)
            for (i in 2 until fractionalPart.length) {
                result.append(" ")
                result.append(SPANISH_NUMBER_NAMES[(fractionalPart[i].code - '0'.code).toLong()])
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
        return if (speech) {
            if (use24Hour) {
                "${time.hour} horas y ${time.minute} minutos"
            } else {
                when {
                    time.hour == 0 && time.minute == 0 -> "medianoche"
                    time.hour == 12 && time.minute == 0 -> "mediodía"
                    else -> {
                        val hour12 = if (time.hour > 12) time.hour - 12 else if (time.hour == 0) 12 else time.hour
                        "$hour12:${String.format("%02d", time.minute)}"
                    }
                }
            }
        } else {
            if (use24Hour) {
                time.format(DateTimeFormatter.ofPattern("HH:mm", Locale("es")))
            } else {
                val hour12 = if (time.hour > 12) time.hour - 12 else if (time.hour == 0) 12 else time.hour
                "$hour12:${String.format("%02d", time.minute)}"
            }
        }
    }

    companion object {
        val SPANISH_NUMBER_NAMES = mapOf(
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
            2000000L to "dos millones",
            1000000000L to "mil millones"
        )
    }
}
