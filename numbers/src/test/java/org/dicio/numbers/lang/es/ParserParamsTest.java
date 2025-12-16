package org.dicio.numbers.lang.es;

import static org.dicio.numbers.test.TestUtils.DAY;
import static org.dicio.numbers.test.TestUtils.F;
import static org.dicio.numbers.test.TestUtils.HOUR;
import static org.dicio.numbers.test.TestUtils.MILLIS;
import static org.dicio.numbers.test.TestUtils.MINUTE;
import static org.dicio.numbers.test.TestUtils.T;
import static org.dicio.numbers.test.TestUtils.YEAR;
import static org.dicio.numbers.test.TestUtils.n;
import static org.dicio.numbers.test.TestUtils.t;

import org.dicio.numbers.parser.Parser;
import org.dicio.numbers.parser.param.NumberParserParamsTestBase;
import org.junit.Test;

import java.time.LocalDateTime;

public class ParserParamsTest extends NumberParserParamsTestBase {

    @Override
    protected Parser numberParser() {
        return new SpanishParser();
    }

    protected void assertNumberFirst(final String s, final boolean preferOrdinal, final Number expectedResult) {
        assertNumberFirst(s, true, preferOrdinal, expectedResult);
    }

    protected void assertNumberMixedWithText(final String s, final boolean preferOrdinal, final Object... expectedResults) {
        assertNumberMixedWithText(s, true, preferOrdinal, expectedResults);
    }

    protected void assertDurationFirst(final String s, final java.time.Duration expectedResult) {
        assertDurationFirst(s, true, expectedResult);
    }

    protected void assertDurationMixedWithText(final String s, final Object... expectedResults) {
        assertDurationMixedWithText(s, true, expectedResults);
    }

    @Test
    public void testNumberFirst() {
        assertNumberFirst("es mil novecientos sesenta y cuatro trilonésimos", F, n(1964e-18, F));
        assertNumberFirst("36 doceavos de manzana", T, n(3, F));
        assertNumberFirst("realmente soy centésimo octavo", F, n(100, F));
        assertNumberFirst("realmente soy centésimo octavo", T, n(108, T));
    }

    @Test
    public void testNumberMixedWithText() {
        assertNumberMixedWithText("un milmillonésimo y mil seiscientos noventa y cuatro", F, n(1.0 / 1000000000.0, F), " y ", n(1694, F));
        assertNumberMixedWithText(" hello  ciao!, 3/5 o cuatro séptimos?", F, " hello  ciao!, ", n(3.0 / 5.0, F), " o ", n(4.0 / 7.0, F), "?");
        assertNumberMixedWithText(" hello  ciao!, cuatro séptimos o 3/5?", T, " hello  ciao!, ", n(4.0 / 7.0, F), " o ", n(3.0 / 5.0, F), "?");
        assertNumberMixedWithText("tres milmillonésimo más dos", T, n(3000000000L, T), " ", n(2, F));
        assertNumberMixedWithText("dos milmillonésimos menos cincuenta y ocho", T, n(2000000000L, T), " ", n(-58, F));
        assertNumberMixedWithText("nueve milmillonésimos por once", F, n(9.0 / 1000000000.0, F), " por ", n(11, F));
        assertNumberMixedWithText("tres mitades, no once cuartos", T, n(3.0 / 2.0, F), ", no ", n(11.0 / 4.0, F));
        assertNumberMixedWithText("seis parejas equivalen a una docena ", T, n(12, F), " equivalen a ", n(12, F), " ");
        assertNumberMixedWithText("6 trilonésimos de una tarta", T, n(6000000000000000000L, T), " de ", n(1, F), " tarta");
        assertNumberMixedWithText("El trillonésimo", F, "El ", n(1000000000000000000L, T));
        assertNumberMixedWithText("Un trillonésimo", F, n(1e-18, F));
        assertNumberMixedWithText("Un billón", T, n(1000000000L, F));
        assertNumberMixedWithText("Mil no más mil", F, n(1000, F), " no ", n(1000, F));
        assertNumberMixedWithText("Gana seis a cero ", T, "Gana ", n(6, F), " a ", n(0, F), " ");
    }

    @Test
    public void testNumberMixedWithTextCompound() {
        assertNumberMixedWithText("milmillón y mil seiscientos noventa y cuatro", F, n(1000.0 / 1000000000.0, F), " y ", n(1694, F));
        assertNumberMixedWithText("es mil novecientos sesenta y cuatro noventa y cuatro trilonésimos", F, "es ", n(1964.0 / 94e18, F));
        assertNumberMixedWithText("veintitrésavo menos cincuenta y ocho veintinueveavos", T, n(23, T), " ", n(-2, F));
        assertNumberMixedWithText("noventa y seis treinta y seisavos más ciento dieciséis", F, n(96.0 / 36.0, F), " ", n(116, F));
        assertNumberMixedWithText("noventa y nueve coma uno uno cero cuatro tres coma cero uno", T, n(99.11043, F), " coma ", n(0, F), n(1, F));
        assertNumberMixedWithText("veinticinco docenas tres cuartos coma veintidos hola", T, n(300, F), " ", n(3.0 / 4.0, F), " coma veintidos hola");
        assertNumberMixedWithText("medias parejas", T, n(0.5, F), " ", n(2, F));
        assertNumberMixedWithText("Tengo veintitrés años", F, "Tengo ", n(23, F), " años");
    }

    @Test
    public void testDurationFirst() {
        assertDurationFirst("Pon un temporizador de dos minutos y treinta segundos test", t(2 * MINUTE + 30));
        assertDurationFirst("sabes dos años atrás no son 750 días", t(2 * YEAR));
    }

    @Test
    public void testDurationMixedWithText() {
        assertDurationMixedWithText("sabes dos años atrás no son 750 días", "sabes ", t(2 * YEAR), " atrás no son ", t(750 * DAY));
        assertDurationMixedWithText("dos ns y cuatro horas mientras seis milisegundos.", t(4 * HOUR, 2), " mientras ", t(0, 6 * MILLIS), ".");
    }

    @Test
    public void testDateTimeFirst() {
        assertDateTimeFirst("cuando en mil novecientos ochenta había", LocalDateTime.of(1, 2, 3, 4, 5, 6), LocalDateTime.of(1980, 1, 1, 4, 5, 6));
        assertDateTimeFirst("dos días atrás era abril?", LocalDateTime.of(2015, 5, 4, 3, 2, 1), LocalDateTime.of(2015, 5, 2, 3, 2, 1));
        assertDateTimeFirst("cuando vie 14 jul del 2017 a las 3:32:00 y después del almuerzo.", LocalDateTime.of(1, 2, 3, 4, 5, 6), LocalDateTime.of(2017, 7, 14, 15, 32, 0));
    }

    @Test
    public void testDateTimeMixedWithText() {
        assertDateTimeMixedWithText("en 1612, no hace diez años!", LocalDateTime.of(1, 2, 3, 4, 5, 6), "en ", LocalDateTime.of(1612, 1, 1, 4, 5, 6), ", no ", LocalDateTime.of(-9, 2, 3, 4, 5, 6), "!");
        assertDateTimeMixedWithText("vie 14 jul del 2017 a las 3:32:00 era después del almuerzo", LocalDateTime.of(9, 8, 7, 6, 5, 4), LocalDateTime.of(2017, 7, 14, 3, 32, 0), " era ", LocalDateTime.of(9, 8, 7, 13, 0, 0));
    }
}
