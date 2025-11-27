package org.dicio.numbers.lang.es

import org.dicio.numbers.formatter.Formatter
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class SpanishFormatter : Formatter() {
    override fun getLanguage(): String = "es"

    override fun formatNumber(number: Long): String {
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

    override fun formatDuration(seconds: Long): String {
        return when {
            seconds < 60 -> "$seconds segundos"
            seconds < 3600 -> "${seconds / 60} minutos"
            else -> "${seconds / 3600} horas"
        }
    }

    override fun formatDateTime(time: LocalDateTime): String {
        return time.toString() // puedes mejorar esto luego
    }
}
