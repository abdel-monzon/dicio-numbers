package org.dicio.numbers

import org.dicio.numbers.lang.SpanishModule

object NumbersModule {
    fun registerAll() {
        SpanishModule.register()
        // otros módulos si los hay
    }
}
