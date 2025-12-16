package org.dicio.numbers.lang.es;

import static org.dicio.numbers.test.TestUtils.F;
import static org.dicio.numbers.test.TestUtils.T;
import static org.dicio.numbers.test.TestUtils.niceDuration;
import static org.dicio.numbers.test.TestUtils.t;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;

import org.dicio.numbers.ParserFormatter;
import org.dicio.numbers.parser.lexer.TokenStream;
import org.dicio.numbers.test.WithTokenizerTestBase;
import org.dicio.numbers.unit.Duration;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;

public class ExtractDateTimeTest extends WithTokenizerTestBase {

    // Jueves 14 de marzo de 2024, 15:30:00
    private static final LocalDateTime NOW = LocalDateTime.of(2024, 3, 14, 15, 30, 0, 0);

    @Override
    public String configFolder() {
        return "config/es-es";
    }


    private void assertRelativeDurationFunction(final String s,
                                                final Duration expectedDuration,
                                                final int finalTokenStreamPosition,
                                                final Function<SpanishDateTimeExtractor, Duration> durationFunction) {
        final TokenStream ts = new TokenStream(tokenizer.tokenize(s));
        final Duration actualDuration = durationFunction.apply(new SpanishDateTimeExtractor(ts, NOW));
        assertNotNull("duración relativa nula para la cadena \"" + s + "\"", actualDuration);
        assertEquals("posición final incorrecta del token para la cadena \"" + s + "\"",
                finalTokenStreamPosition, ts.position);
        assertTrue("duración relativa incorrecta para la cadena \"" + s + "\": se esperaba \""
                        + niceDuration(expectedDuration) + "\" pero se obtuvo \""
                        + niceDuration(actualDuration) + "\"",
                expectedDuration.nanos == actualDuration.nanos
                        && expectedDuration.days == actualDuration.days
                        && expectedDuration.months == actualDuration.months
                        && expectedDuration.years == actualDuration.years);
    }

    private void assertRelativeDurationFunctionNull(final String s,
                                                    final Function<SpanishDateTimeExtractor, Duration> durationFunction) {
        final TokenStream ts = new TokenStream(tokenizer.tokenize(s));
        final Duration duration = durationFunction.apply(new SpanishDateTimeExtractor(ts, NOW));

        if (duration != null) {
            fail("se esperaba que no hubiera duración relativa (null), pero se obtuvo \"" + niceDuration(duration)
                    + "\"");
        }
    }

    private <T> void assertFunction(final String s,
                                    final T expectedResult,
                                    int finalTokenStreamPosition,
                                    final Function<SpanishDateTimeExtractor, T> function) {
        final TokenStream ts = new TokenStream(tokenizer.tokenize(s));
        assertEquals("resultado incorrecto para la cadena \"" + s + "\"",
                expectedResult, function.apply(new SpanishDateTimeExtractor(ts, NOW)));
        assertEquals("posición final incorrecta del token para la cadena \"" + s + "\"",
                finalTokenStreamPosition, ts.position);
    }

    private <T> void assertFunctionNull(final String s,
                                        final Function<SpanishDateTimeExtractor, T> numberFunction) {
        assertFunction(s, null, 0, numberFunction);
    }

    private void assertRelativeDuration(final String s, final Duration expectedDuration, int finalTokenStreamPosition) {
        assertRelativeDurationFunction(s, expectedDuration, finalTokenStreamPosition, SpanishDateTimeExtractor::relativeDuration);
    }

    private void assertRelativeDurationNull(final String s) {
        assertRelativeDurationFunctionNull(s, SpanishDateTimeExtractor::relativeDuration);
    }

    private void assertRelativeTomorrow(final String s, final int expectedDuration, int finalTokenStreamPosition) {
        assertFunction(s, expectedDuration, finalTokenStreamPosition, SpanishDateTimeExtractor::relativeTomorrow);
    }

    private void assertRelativeTomorrowNull(final String s) {
        assertFunctionNull(s, SpanishDateTimeExtractor::relativeTomorrow);
    }

    private void assertRelativeYesterday(final String s, final int expectedDuration, int finalTokenStreamPosition) {
        assertFunction(s, expectedDuration, finalTokenStreamPosition, SpanishDateTimeExtractor::relativeYesterday);
    }

    private void assertRelativeYesterdayNull(final String s) {
        assertFunctionNull(s, SpanishDateTimeExtractor::relativeYesterday);
    }

    private void assertHour(final String s, final int expected, int finalTokenStreamPosition) {
        assertFunction(s, expected, finalTokenStreamPosition, SpanishDateTimeExtractor::hour);
    }

    private void assertHourNull(final String s) {
        assertFunctionNull(s, SpanishDateTimeExtractor::hour);
    }

    private void assertMomentOfDay(final String s, final int expected, int finalTokenStreamPosition) {
        assertFunction(s, expected, finalTokenStreamPosition, SpanishDateTimeExtractor::momentOfDay);
    }

    private void assertMomentOfDayNull(final String s) {
        assertFunctionNull(s, SpanishDateTimeExtractor::momentOfDay);
    }

    private void assertNoonMidnightLike(final String s, final int expected, int finalTokenStreamPosition) {
        assertFunction(s, expected, finalTokenStreamPosition, SpanishDateTimeExtractor::noonMidnightLike);
    }

    private void assertNoonMidnightLikeNull(final String s) {
        assertFunctionNull(s, SpanishDateTimeExtractor::noonMidnightLike);
    }

    private void assertSpecialMinute(final String s, final int expected, int finalTokenStreamPosition) {
        assertFunction(s, expected, finalTokenStreamPosition, SpanishDateTimeExtractor::specialMinute);
    }

    private void assertSpecialMinuteNull(final String s) {
        assertFunctionNull(s, SpanishDateTimeExtractor::specialMinute);
    }

    private void assertOClock(final String s, int finalTokenStreamPosition) {
        assertFunction(s, true, finalTokenStreamPosition, SpanishDateTimeExtractor::oClock);
    }

    private void assertOClockFalse(final String s) {
        assertFunction(s, false, 0, SpanishDateTimeExtractor::oClock);
    }

    private void assertDate(final String s, final LocalDate expected, int finalTokenStreamPosition) {
        assertFunction(s, expected, finalTokenStreamPosition, SpanishDateTimeExtractor::date);
    }

    private void assertDateNull(final String s) {
        assertFunctionNull(s, SpanishDateTimeExtractor::date);
    }

    private void assertTime(final String s, final LocalTime expected, int finalTokenStreamPosition) {
        assertFunction(s, expected, finalTokenStreamPosition, SpanishDateTimeExtractor::time);
    }

    private void assertTimeNull(final String s) {
        assertFunctionNull(s, SpanishDateTimeExtractor::time);
    }

    private void assertTimeWithAmpm(final String s, final LocalTime expected, int finalTokenStreamPosition) {
        assertFunction(s, expected, finalTokenStreamPosition, SpanishDateTimeExtractor::timeWithAmpm);
    }

    private void assertTimeWithAmpmNull(final String s) {
        assertFunctionNull(s, SpanishDateTimeExtractor::timeWithAmpm);
    }

    private void assertDateTime(final String s, final LocalDateTime expected, int finalTokenStreamPosition) {
        assertFunction(s, expected, finalTokenStreamPosition, SpanishDateTimeExtractor::dateTime);
    }

    private void assertDateTimeNull(final String s) {
        assertFunctionNull(s, SpanishDateTimeExtractor::dateTime);
    }


    @Test
    public void testRelativeDuration() {
        assertRelativeDuration("en dos semanas iré",   t(2, WEEKS),   3);
        assertRelativeDuration("hace cuatro meses",     t(-4, MONTHS), 3);
        assertRelativeDuration("después de segundos se cayó",  t(1, SECONDS), 3);
        assertRelativeDuration("en un par de décadas", t(20, YEARS),  5);
        assertRelativeDuration("nueve días atrás",     t(-9, DAYS),   3);
        assertRelativeDuration("setenta años atrás",   t(-70, YEARS), 3);
        assertRelativeDuration("tres meses y dos días antes", t(-3, MONTHS).plus(t(-2, DAYS)), 6);
        assertRelativeDuration("últimos sesenta y siete siglos comenzaron hace seis mil setecientos años", t(-6700, YEARS), 5);
    }

    @Test
    public void testRelativeDurationNull() {
        assertRelativeDurationNull("hola qué tal");
        assertRelativeDurationNull("cuatro semestres");
        assertRelativeDurationNull("sabes que en una semana");
        assertRelativeDurationNull("y dos meses atrás");
        assertRelativeDurationNull("el día pasado");
    }

    @Test
    public void testRelativeTomorrow() {
        assertRelativeTomorrow("mañana vamos",             1, 1);
        assertRelativeTomorrow("pasado mañana y",          2, 2);
        assertRelativeTomorrow("día después de mañana",    2, 4);
        assertRelativeTomorrow("mañana el día después",    1, 1);
    }

    @Test
    public void testRelativeTomorrowNull() {
        assertRelativeTomorrowNull("hola qué tal");
        assertRelativeTomorrowNull("la mañana");
        assertRelativeTomorrowNull("de día después de mañana");
        assertRelativeTomorrowNull("ayer");
        assertRelativeTomorrowNull("hoy");
        assertRelativeTomorrowNull("día después del mañana");
    }

    @Test
    public void testRelativeYesterday() {
        assertRelativeYesterday("ayer estuve",          -1, 1);
        assertRelativeYesterday("anteayer y",           -2, 2);
        assertRelativeYesterday("día antes de ayer",    -2, 4);
        assertRelativeYesterday("ayer el día antes",    -1, 1);
    }

    @Test
    public void testRelativeYesterdayNull() {
        assertRelativeYesterdayNull("hola qué tal");
        assertRelativeYesterdayNull("y ayer");
        assertRelativeYesterdayNull("de día antes de mañana");
        assertRelativeYesterdayNull("hoy");
        assertRelativeYesterdayNull("mañana");
        assertRelativeYesterdayNull("día antes del mañana");
    }

    @Test
    public void testHour() {
        assertHour("8:36 test",          8,  1);
        assertHour("16:44 test",         16, 1);
        assertHour("veintiuno test",     21, 2);
        assertHour("las cero y",         0,  2);
        assertHour("a la una veintiséis", 1,  2);
        assertHour("doce en punto",      12, 1);
        assertHour("a las diecisiete",   17, 2);
        assertHour("a horas tres",       3,  4);
        assertHour("a horas trece",      13, 3);
        assertHour("las siete test",     7,  2);
    }

    @Test
    public void testHourNull() {
        assertHourNull("hola qué tal");
        assertHourNull("veinticinco");
        assertHourNull("las menos dos");
        assertHourNull("a las ciento cincuenta y cuatro");
        assertHourNull("a hora");
        assertHourNull("las y cero y");
        assertHourNull("y veinticuatro");
        assertHourNull("las un millón");
    }

    @Test
    public void testNoonMidnightLike() {
        assertNoonMidnightLike("a medianoche", 0,  2);
        assertNoonMidnightLike("mediodías",    12, 1);
        assertNoonMidnightLike("este mediodía", 12, 2);
    }

    @Test
    public void testNoonMidnightLikeNull() {
        assertNoonMidnightLikeNull("hola qué tal");
        assertNoonMidnightLikeNull("esta tarde y");
        assertNoonMidnightLikeNull("esta noche test");
        assertNoonMidnightLikeNull("después de cenar");
        assertNoonMidnightLikeNull("antes del almuerzo");
        assertNoonMidnightLikeNull("y a mediodía");
        assertNoonMidnightLikeNull("y medianoche");
        assertNoonMidnightLikeNull("a hora mediodía");
        assertNoonMidnightLikeNull("en medianoche");
        assertNoonMidnightLikeNull("a la mediodía");
    }

    @Test
    public void testMomentOfDay() {
        assertMomentOfDay("a medianoche",      0,  2);
        assertMomentOfDay("mediodía",          12, 1);
        assertMomentOfDay("estas medianoches", 0,  3);
        assertMomentOfDay("esta tarde y",      15, 2);
        assertMomentOfDay("esta noche test",   21, 2);
        assertMomentOfDay("madrugada test",    3,  1);
        assertMomentOfDay("después de cenar",  21, 2);
        assertMomentOfDay("antes del almuerzo", 11, 3);
    }

    @Test
    public void testMomentOfDayNull() {
        assertMomentOfDayNull("hola qué tal");
        assertMomentOfDayNull("y a mediodía");
        assertMomentOfDayNull("media noche");
        assertMomentOfDayNull("a hora cena");
        assertMomentOfDayNull("en cena");
    }

    @Test
    public void testSpecialMinute() {
        assertSpecialMinute("un cuarto para",           -15, 3);
        assertSpecialMinute("media pasada test",        30,  3);
        assertSpecialMinute("media para las once",      -30, 3);
        assertSpecialMinute("cero punto dos pasadas",   12,  5);
        assertSpecialMinute("trece catorceavos para",   -56, 3); // 13/14*60 es 55.7 -> redondeado a 56
        assertSpecialMinute("las veinte pasadas",       20,  4);
        assertSpecialMinute("las cincuenta y nueve para", -59, 5);
        assertSpecialMinute("quince pasadas las doce",  15,  2);
    }

    @Test
    public void testSpecialMinuteNull() {
        assertSpecialMinuteNull("hola qué tal");
        assertSpecialMinuteNull("dos");
        assertSpecialMinuteNull("ciento doce para");
        assertSpecialMinuteNull("menos un cuarto para las cinco");
        assertSpecialMinuteNull("cuatro cuartos para las nueve");
        assertSpecialMinuteNull("cero medios para");
        assertSpecialMinuteNull("cero y coma dos pasadas");
        assertSpecialMinuteNull("trece y catorceavos pasadas");
        assertSpecialMinuteNull("y quince pasadas las doce");
    }

    @Test
    public void testOClock() {
        assertOClock("en punto",    2);
        assertOClock("exactas",     1);
        assertOClock("y cero",      2);
        assertOClock("en punto y",  3);
    }

    @Test
    public void testOClockFalse() {
        assertOClockFalse("hola");
        assertOClockFalse("por el reloj");
        assertOClockFalse("reloj en");
        assertOClockFalse("reloj");
        assertOClockFalse("en");
    }

    @Test
    public void testDate() {
        assertDate("04/09-4096",                                 LocalDate.of(4096,  9,  4),  5);
        assertDate("26/12/2026",                                LocalDate.of(2026, 12,  26), 5);
        assertDate("viernes 26 de mayo 2022",                   LocalDate.of(2022,  5,  26), 5);
        assertDate("lun doce jun mil doce antes de Cristo",     LocalDate.of(-2012, 6,  12), 8);
        assertDate("cuatrocientos setenta y seis d. C.",        LocalDate.of(476,   1,  1),  7);
        assertDate("cuatro mil antes de Cristo",                LocalDate.of(-4000, 1,  1),  4);
        assertDate("cuatro mil del antes Cristo",               LocalDate.of(4000,  1,  1),  2);
        assertDate("jueves y veintisiete",                      LocalDate.of(2024,  3,  21), 3);
        assertDate("dos mil doce",                              LocalDate.of(2012,  1,  1),  3);
        assertDate("marzo e",                                   LocalDate.of(2024,  3,  1),  1);
        assertDate("miércoles test ocho",                       LocalDate.of(2024,  3,  8),  1);
        assertDate("lunes marzo",                               LocalDate.of(2024,  2,  26), 1);
        assertDate("octubre dos mil doce",                      LocalDate.of(2012,  10, 1),  5);
        assertDate("999999999",                                  LocalDate.of(999999999,1,1), 1);
        assertDate("veinte veintidos",                           LocalDate.of(2022,  1,  1),  2);
        assertDate("domingo veintitres",                         LocalDate.of(2024,  3,  17), 1);
    }

    @Test
    public void testDateNull() {
        assertDateNull("hola qué tal");
        assertDateNull("soy jueves");
        assertDateNull("y dos mil quince");
        assertDateNull("del dos de mayo");
        assertDateNull("mañana");
        assertDateNull("1000000000");
    }

    @Test
    public void testTime() {
        assertTime("13:28.33 test",                          LocalTime.of(13, 28, 33), 4);
        assertTime("mediodía y media",                       LocalTime.of(12, 30, 0),  3);
        assertTime("a las catorce y",                        LocalTime.of(14, 0,  0),  2);
        assertTime("las veintitrés y cincuenta y un minutos y 17 segundos", LocalTime.of(23, 51, 17), 10);
        assertTime("medianoche del doce",                    LocalTime.of(0,  12, 0),  3);
        assertTime("veinticuatro y cero",                    LocalTime.of(0,  0,  0),  4);
    }

    @Test
    public void testTimeNull() {
        assertTimeNull("hola qué tal");
        assertTimeNull("sesenta y uno");
        assertTimeNull("30:59");
        assertTimeNull("menos dieciséis");
        assertTimeNull("cuatro millones");
        assertTimeNull("tarde");
    }

    @Test
    public void testTimeWithAmpm() {
        assertTimeWithAmpm("11:28.33 p. m. test",                      LocalTime.of(23, 28, 33), 5);
        assertTimeWithAmpm("mediodía y media después del almuerzo",    LocalTime.of(12, 30, 0),  5);
        assertTimeWithAmpm("a las dos de la noche",                   LocalTime.of(2,  0,  0),  4);
        assertTimeWithAmpm("las tres y treinta y ocho de la tarde",   LocalTime.of(15, 38, 0),  7);
        assertTimeWithAmpm("18:29:02 y am",                           LocalTime.of(18, 29, 2),  5);
        assertTimeWithAmpm("tarde",                                   LocalTime.of(15, 0,  0),  1);
        assertTimeWithAmpm("por la tarde a las cuatro y tres y seis", LocalTime.of(16, 3,  6),  7);
        assertTimeWithAmpm("las veinticuatro de la tarde",            LocalTime.of(0,  0,  0),  5);
        assertTimeWithAmpm("12 am",                                   LocalTime.of(12, 0,  0),  2);
    }

    @Test
    public void testTimeWithAmpmNull() {
        assertTimeWithAmpmNull("hola qué tal");
        assertTimeWithAmpmNull("sesenta y uno");
        assertTimeWithAmpmNull("30:59");
        assertTimeWithAmpmNull("menos dieciséis");
        assertTimeWithAmpmNull("cuatro millones");
    }

    @Test
    public void testDateTime() {
        assertDateTime("mañana a las 12:45",                                 LocalDateTime.of(2024, 3,  15, 12, 45, 0),  4);
        assertDateTime("26/12/2003 19:18:59",                               LocalDateTime.of(2003, 12, 26, 19, 18, 59), 8);
        assertDateTime("19:18:59 26/12/2003",                               LocalDateTime.of(2003, 12, 26, 19, 18, 59), 8);
        assertDateTime("lunes próximo a las veintidos",                     LocalDateTime.of(2024, 3,  18, 22, 0,  0),  5);
        assertDateTime("las 6 p. m. del martes próximo",                    LocalDateTime.of(2024, 3,  19, 18, 0,  0),  7);
        assertDateTime("veintisiete de julio a las nueve y treinta y nueve de la noche", LocalDateTime.of(2024, 7,  27, 21, 39, 0),  10);
        assertDateTime("ayer a las 5 y media",                              LocalDateTime.of(2024, 3,  13, 17, 30, 0),  6);
        assertDateTime("mañana por la noche a las once",                    LocalDateTime.of(2024, 3,  15, 23, 0,  0),  4);
        assertDateTime("ayer y mañana test",                                LocalDateTime.of(2024, 3,  13, 9,  0,  0),  3);
        assertDateTime("domingo a las 2:45 p. m.",                          LocalDateTime.of(2024, 3,  17, 14, 45, 0),  7);
        assertDateTime("veintiuno de enero después de cenar",               LocalDateTime.of(2024, 1,  21, 21, 0,  0),  6);
        assertDateTime("dentro de dos días a las quince",                   LocalDateTime.of(2024, 3,  16, 15, 0,  0),  7);
        assertDateTime("el próximo periodo en el próximo mes",              LocalDateTime.of(2024, 3,  14, 15, 0,  0),  6);
    }

    @Test
    public void testDateTimeNull() {
        assertDateTimeNull("hola qué tal");
        assertDateTimeNull("marzo");
        assertDateTimeNull("cinco de marzo");
        assertDateTimeNull("mañana mañana");
        assertDateTimeNull("ayer y");
        assertDateTimeNull("en dos años");
        assertDateTimeNull("en las noticias");
        assertDateTimeNull("24 de 12 2022");
    }
}
