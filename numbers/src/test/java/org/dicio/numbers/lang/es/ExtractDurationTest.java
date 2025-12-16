package org.dicio.numbers.lang.es;

import static org.dicio.numbers.test.TestUtils.DAY;
import static org.dicio.numbers.test.TestUtils.MONTH;
import static org.dicio.numbers.test.TestUtils.t;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.dicio.numbers.ParserFormatter;
import org.dicio.numbers.test.WithTokenizerTestBase;
import org.junit.Test;

public class ExtractDurationTest extends WithTokenizerTestBase {
    @Override
    public String configFolder() {
        return "config/es-es";
    }

    @Test
    public void testNumberParserExtractDuration() {
        final ParserFormatter npf = new ParserFormatter(null, new SpanishParser());
        assertNull(npf.extractDuration("hola qué tal").getFirst());
        assertNull(npf.extractDuration("mil millones de euros").shortScale(true).getFirst());
        assertNull(npf.extractDuration("un millón").shortScale(false).getFirst());
        assertEquals(t(DAY), npf.extractDuration("veinticuatro horas no son dos días").getFirst().toJavaDuration());
        assertEquals(t(2 * DAY), npf.extractDuration("dos días no son veinticuatro horas").getFirst().toJavaDuration());
        assertEquals(t(3 * MONTH + 2 * DAY), npf.extractDuration("tres meses y dos días").getFirst().toJavaDuration());
    }
}
