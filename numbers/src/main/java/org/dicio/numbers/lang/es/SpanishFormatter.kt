package org.dicio.numbers.lang.es

import org.dicio.numbers.formatter.Formatter
import org.dicio.numbers.unit.MixedFraction
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class SpanishFormatter : Formatter {
    override fun getLanguage() = "es"

    override fun formatNumber(
        number: Long,
        flags: Int
    ): String {
        return when {
            flags and FLAG_SPEECH != 0 -> formatNumberSpeech(number)
            flags and FLAG_ORDINAL != 0 -> formatOrdinal(number)
            else -> number.toString()
        }
    }

    override fun formatDuration(
        duration: Long,
        unit: ChronoUnit,
        speech: Boolean
    ): String {
        return "$duration ${unit.name.lowercase()}"
    }

    override fun formatDateTime(
        dateTime: LocalDateTime,
        speech: Boolean
    ): String {
        return dateTime.toString()
    }

    override fun niceNumber(
        mixedFraction: MixedFraction,
        speech: Boolean
    ): String {
        return mixedFraction.toString()
    }

    private fun formatNumberSpeech(number: Long): String {
        return when (number) {
            0L -> "cero"
            1L -> "uno"
            2L -> "dos"
            3L -> "tres"
            4L -> "cuatro"
            5L -> "cinco"
            6L -> "seis"
            7L -> "siete"
            8L -> "ocho"
            9L -> "nueve"
            10L -> "diez"
            else -> number.toString()
        }
    }

    private fun formatOrdinal(number: Long): String {
        return when (number) {
            1L -> "primero"
            2L -> "segundo"
            3L -> "tercero"
            else -> "${number}º"
        }
    }
}
