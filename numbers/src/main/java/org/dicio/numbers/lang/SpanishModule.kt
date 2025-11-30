package org.dicio.numbers.lang

import org.dicio.numbers.formatter.FormatterFactory
import org.dicio.numbers.lang.es.SpanishFormatterFactory
import org.dicio.numbers.parser.ParserFactory
import org.dicio.numbers.lang.es.SpanishParser

object SpanishModule {
    fun register() {
        FormatterFactory.register("es", SpanishFormatterFactory())
        ParserFactory.register("es", SpanishParser())
    }
}
