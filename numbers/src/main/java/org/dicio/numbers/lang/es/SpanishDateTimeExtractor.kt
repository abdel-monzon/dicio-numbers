package org.dicio.numbers.lang.es

import org.dicio.numbers.parser.lexer.TokenStream
import org.dicio.numbers.unit.Duration
import org.dicio.numbers.unit.Number
import java.time.LocalDateTime

class SpanishDateTimeExtractor internal constructor(
    private val ts: TokenStream,
    private val shortScale: Boolean,
    private val preferMonthBeforeDay: Boolean,
    private val now: LocalDateTime
) {
    fun dateTime(): LocalDateTime? {
        // Implementación temporal - necesita lógica completa para español
        return null
    }

    fun duration(): Duration? {
        // Implementación temporal - necesita lógica completa para español
        return null
    }
}
