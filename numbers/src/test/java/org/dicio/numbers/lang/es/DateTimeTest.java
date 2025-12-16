package org.dicio.numbers.lang.es;

import org.dicio.numbers.formatter.Formatter;
import org.dicio.numbers.test.DateTimeTestBase;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class DateTimeTest extends DateTimeTestBase {

    @Override
    public String configFolder() {
        return "config/es-es";
    }

    @Override
    public Formatter buildNumberFormatter() {
        return new SpanishFormatter();
    }

    @Test
    public void testNiceDate() {
        // Solo verifica que las funciones de NumberParserFormatter hagan su trabajo
        assertEquals("miércoles, veintiocho de abril de dos mil veintiuno",
                pf.niceDate(LocalDate.of(2021, 4, 28)).get());
        assertEquals("domingo, trece de agosto",
                pf.niceDate(LocalDate.of(-84, 8, 13)).now(LocalDate.of(-84, 8, 23)).get());
    }

    @Test
    public void testNiceYear() {
        // Solo verifica que las funciones de NumberParserFormatter hagan su trabajo
        assertEquals("mil novecientos ochenta y cuatro", pf.niceYear(LocalDate.of(1984, 4, 28)).get());
        assertEquals("ochocientos diez a. C.", pf.niceYear(LocalDate.of(-810, 8, 13)).get());
    }

    @Test
    public void testNiceDateTime() {
        // Solo verifica que las funciones de NumberParserFormatter hagan su trabajo
        assertEquals("miércoles, doce de septiembre de mil setecientos sesenta y cuatro al mediodía", 
                pf.niceDateTime(LocalDateTime.of(1764, 9, 12, 12, 0)).get());
        assertEquals("jueves, tres de noviembre de trescientos veintiocho a. C. a las cinco y siete", 
                pf.niceDateTime(LocalDateTime.of(-328, 11, 3, 5, 7)).get());
    }
}
