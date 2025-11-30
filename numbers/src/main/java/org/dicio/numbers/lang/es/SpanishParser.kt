package org.dicio.numbers.lang.es

import org.dicio.numbers.parser.NumberParser
import org.dicio.numbers.parser.NumberWords

class SpanishParser : NumberParser {
    override fun getLanguage() = "es"
    override fun getNumberWords() = NumberWords(
        // aquí irían las palabras numéricas españolas
        mapOf(
            "cero" to 0L,
            "uno" to 1L,
            "dos" to 2L,
            "tres" to 3L,
            "cuatro" to 4L,
            "cinco" to 5L
        ),
        mapOf(
            "primero" to 1L,
            "segundo" to 2L,
            "tercero" to 3L
        )
    )

    override fun getOrdinalWords() = mapOf(
        "primero" to 1L,
        "segundo" to 2L,
        "tercero" to 3L
    )
}
