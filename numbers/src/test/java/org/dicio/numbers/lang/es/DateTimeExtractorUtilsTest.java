package org.dicio.numbers.lang.es;

import static org.dicio.numbers.test.TestUtils.t;
import static org.dicio.numbers.util.NumberExtractorUtils.signBeforeNumber;
import static java.time.temporal.ChronoUnit.MONTHS;

import org.dicio.numbers.parser.lexer.TokenStream;
import org.dicio.numbers.test.DateTimeExtractorUtilsTestBase;
import org.dicio.numbers.util.DateTimeExtractorUtils;
import org.dicio.numbers.util.NumberExtractorUtils;
import org.junit.Test;

import java.time.LocalDateTime;

public class DateTimeExtractorUtilsTest extends DateTimeExtractorUtilsTestBase {

    // Martes 15 de agosto de 2023, 14:30:00
    private static final LocalDateTime NOW = LocalDateTime.of(2023, 8, 15, 14, 30, 0);

    @Override
    public String configFolder() {
        return "config/es-es";
    }

    @Override
    public DateTimeExtractorUtils build(final TokenStream ts) {
        final SpanishNumberExtractor numberExtractor = new SpanishNumberExtractor(ts);
        return new DateTimeExtractorUtils(ts, NOW, (fromInclusive, toInclusive) ->
            NumberExtractorUtils.extractOneIntegerInRange(ts, fromInclusive, toInclusive,
                    () -> signBeforeNumber(ts, () -> numberExtractor.numberInteger(false)))
        );
    }

    @Test
    public void testRelativeMonthDuration() {
        assertRelativeMonthDuration("próximo septiembre",         t(1, MONTHS),   2);
        assertRelativeMonthDuration("abril siguiente",           t(-4, MONTHS),  2);
        assertRelativeMonthDuration("último abril",              t(-4, MONTHS),  2);
        assertRelativeMonthDuration("febrero próximo",           t(-6, MONTHS),  2);
        assertRelativeMonthDuration("febrero pasado",            t(-6, MONTHS),  2);
        assertRelativeMonthDuration("hace enero",                t(-7, MONTHS),  2);
    }

    @Test
    public void testRelativeMonthDurationNull() {
        assertRelativeMonthDurationNull("hola qué tal");
        assertRelativeMonthDurationNull("este noviembre siguiente");
        assertRelativeMonthDurationNull("octubre");
        assertRelativeMonthDurationNull("en dos octubre");
        assertRelativeMonthDurationNull("en dos meses");
    }

    @Test
    public void testRelativeDayOfWeekDuration() {
        assertRelativeDayOfWeekDuration("próximo jueves",               5,   2);
        assertRelativeDayOfWeekDuration("último jueves",                -3,  2);
        assertRelativeDayOfWeekDuration("dos domingos pasados",         -12, 3);
        assertRelativeDayOfWeekDuration("tres martes y siguiente",      11,  4);
        assertRelativeDayOfWeekDuration("cuatro lunes antes",           -25, 4);
        assertRelativeDayOfWeekDuration("próximo sábado",               7,   2);
        assertRelativeDayOfWeekDuration("hace sábado",                  -7,  2);
    }

    @Test
    public void testRelativeDayOfWeekDurationNull() {
        assertRelativeDayOfWeekDurationNull("hola qué tal");
        assertRelativeDayOfWeekDurationNull("lunes");
        assertRelativeDayOfWeekDurationNull("hace lunes");
        assertRelativeDayOfWeekDurationNull("dos viernes");
        assertRelativeDayOfWeekDurationNull("en dos días");
        assertRelativeDayOfWeekDurationNull("y en dos domingos");
        assertRelativeDayOfWeekDurationNull("ayer y mañana");
    }

    @Test
    public void testRelativeToday() {
        assertRelativeToday("hoy");
        assertRelativeToday("hoy justo hoy");
        assertRelativeToday("hoy prueba");
        assertRelativeToday("hoy y");
    }

    @Test
    public void testRelativeTodayNull() {
        assertRelativeTodayNull("hola qué tal");
        assertRelativeTodayNull("justo hoy");
        assertRelativeTodayNull("el hoy");
        assertRelativeTodayNull("y hoy");
        assertRelativeTodayNull("ayer");
        assertRelativeTodayNull("mañana");
    }

    @Test
    public void testMinute() {
        assertMinute("cero a b c",          0,  1);
        assertMinute("cincuenta y nueve horas", 59, 2);
        assertMinute("quince y",            15, 1);
        assertMinute("veinte y ocho s",     28, 3);
        assertMinute("seis mins prueba",    6,  2);
        assertMinute("treinta y seis de min", 36, 2);
        assertMinute("44m de",              44, 2);
    }

    @Test
    public void testMinuteNull() {
        assertMinuteNull("hola qué tal");
        assertMinuteNull("sesenta minutos");
        assertMinuteNull("ciento veinte");
        assertMinuteNull("menos dieciséis");
        assertMinuteNull("12000 minutos");
        assertMinuteNull("y dos de");
    }

    @Test
    public void testSecond() {
        assertSecond("cero a b c",          0,  1);
        assertSecond("cincuenta y nueve horas", 59, 2);
        assertSecond("quince y",            15, 1);
        assertSecond("veinte y ocho m",     28, 3);
        assertSecond("seis segundos prueba", 6,  2);
        assertSecond("treinta y seis de seg", 36, 2);
        assertSecond("44s de",              44, 2);
    }

    @Test
    public void testSecondNull() {
        assertSecondNull("hola qué tal");
        assertSecondNull("sesenta segundos");
        assertSecondNull("ciento veinte");
        assertSecondNull("menos dieciséis");
        assertSecondNull("12000 segundos");
        assertSecondNull("doce mil");
        assertSecondNull("y dos de");
    }

    @Test
    public void testBcad() {
        assertBcad("a. C. prueba",     false, 3);
        assertBcad("d. C. y",          true,  3);
        assertBcad("dc prueba de",     true,  1);
        assertBcad("antes de Cristo",  false, 3);
        assertBcad("a y Domini",       true,  3);
        assertBcad("a.C.",             false, 1);
        assertBcad("a actual",         false, 2);
    }

    @Test
    public void testBcadNull() {
        assertBcadNull("a.m.");
        assertBcadNull("después test Cristo");
        assertBcadNull("y antes Cristo");
        assertBcadNull("test c");
        assertBcadNull("m");
        assertBcadNull("c test");
    }

    @Test
    public void testAmpm() {
        assertAmpm("a.m. prueba",        false, 3);
        assertAmpm("p.m. y",             true,  3);
        assertAmpm("am y prueba",        false, 1);
        assertAmpm("post meridiano",     true,  2);
        assertAmpm("p y meridiem",       true,  3);
    }

    @Test
    public void testAmpmNull() {
        assertAmpmNull("a. C.");
        assertAmpmNull("ante test meridiem");
        assertAmpmNull("y post m");
        assertAmpmNull("test m");
        assertAmpmNull("c");
        assertAmpmNull("aem");
        assertAmpmNull("meridian test");
    }

    @Test
    public void testMonthName() {
        assertMonthName("enero",      1);
        assertMonthName("dic e",      12);
        assertMonthName("sep tiembre", 9);
        assertMonthName("mar",        3);
    }

    @Test
    public void testMonthNameNull() {
        assertMonthNameNull("lunes");
        assertMonthNameNull("jaguar");
        assertMonthNameNull("hola feb");
        assertMonthNameNull("y dic de");
    }
          }
