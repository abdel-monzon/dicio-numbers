package org.dicio.numbers.lang

import org.dicio.numbers.formatter.FormatterFactory
import org.dicio.numbers.lang.es.SpanishFormatterFactory

object SpanishModule {
    fun register() {
        FormatterFactory.register("es", SpanishFormatterFactory())
    }
}
