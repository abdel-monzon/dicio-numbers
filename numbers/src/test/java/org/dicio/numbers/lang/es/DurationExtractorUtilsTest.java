package org.dicio.numbers.lang.es;

import static org.dicio.numbers.test.TestUtils.DAY;
import static org.dicio.numbers.test.TestUtils.F;
import static org.dicio.numbers.test.TestUtils.HOUR;
import static org.dicio.numbers.test.TestUtils.MICROS;
import static org.dicio.numbers.test.TestUtils.MILLIS;
import static org.dicio.numbers.test.TestUtils.MINUTE;
import static org.dicio.numbers.test.TestUtils.MONTH;
import static org.dicio.numbers.test.TestUtils.WEEK;
import static org.dicio.numbers.test.TestUtils.YEAR;
import static org.dicio.numbers.test.TestUtils.t;
import static org.junit.Assert.assertTrue;

import org.dicio.numbers.ParserFormatter;
import org.dicio.numbers.parser.lexer.TokenStream;
import org.dicio.numbers.test.DurationExtractorUtilsTestBase;
import org.dicio.numbers.unit.Duration;
import org.dicio.numbers.util.DurationExtractorUtils;
import org.junit.Test;

/**
 * TODO también probar extractDurationAtCurrentPosition
 */
public class DurationExtractorUtilsTest extends DurationExtractorUtilsTestBase {

    @Override
    public String configFolder() {
        return "config/es-es";
    }

    @Override
    public Duration extractDuration(final TokenStream ts, final boolean shortScale) {
        final SpanishNumberExtractor numberExtractor = new SpanishNumberExtractor(ts);
        return new DurationExtractorUtils(ts, numberExtractor::numberNoOrdinal).duration();
    }

    @Test
    public void testDurationNumberAndUnit() {
        assertDuration("mil millones de nanosegundos",   F, t(1000));
        assertDuration("mil millones de nanosegundos",   T, t(1));
        assertDuration("mil setecientos veintiocho μs",  F, t(0, 1728 * MICROS));
        assertDuration("una décima de milisegundo",      T, t(0, 100 * MICROS));
        assertDuration("18s",                            F, t(18));
        assertDuration("un seg",                         F, t(1));
        assertDuration("cincuenta y nueve minutos",      T, t(59 * MINUTE));
        assertDuration("veintitrés horas",               F, t(23 * HOUR));
        assertDuration("media hora",                     T, t(HOUR / 2));
        assertDuration("uno punto dos días",             T, t(1.2 * DAY));
        assertDuration("medio día",                      F, t(DAY / 2));
        assertDuration("diez y semanas y",               F, t(10 * WEEK));
        assertDuration("6 meses",                        T, t(6 * MONTH));
        assertDuration("tres mil millones de años atrás",T, t(3e9 * YEAR));
        assertDuration("quince décadas",                 T, t(150 * YEAR));
        assertDuration("una milésima de siglo",          F, t(1e-12 * 100 * YEAR));
        assertDuration("una milésima de siglo",          T, t(1e-9 * 100 * YEAR));
        assertDuration("1 milenio",                      F, t(1000 * YEAR));
        assertNoDuration("cuatro tres milenios cuatro", T);
        assertNoDuration("y diez y semanas y",          F);
        assertNoDuration("cien pruebas",                F);
        assertNoDuration("punto tres cuatro gramos",    T);
    }

    @Test
    public void testDurationOnlyUnit() {
        assertDuration("hora minuto milenio",                              T, t(1000 * YEAR + HOUR + MINUTE));
        assertDuration("milisegundo y segundo, microsegundo",              F, t(1, MILLIS + MICROS));
        assertDuration("segundos segundo s",                               T, t(2));
        assertDuration("minuto horas año",                                 F, t(MINUTE + HOUR));
        assertNoDuration("hola milisegundo",         F);
        assertNoDuration("es bueno",                 T);
        assertNoDuration("ns μs ms s m h d sem mes año", F);
    }

    @Test
    public void testDurationOf() {
        assertDuration("dos décimas de segundo",       F, t(0, 200 * MILLIS));
        assertDuration("un par de horas",              F, t(2 * HOUR));
        assertNoDuration("muchos segundos",                    F);
        assertNoDuration("decenas de líneas de pruebas",        T);
        assertNoDuration("hola doscientos de hola",              F);
        assertNoDuration("hola de s",                            F);
    }

    @Test
    public void testMultipleDurationGroups() {
        assertDuration("veinte minutos y treinta y seis y segundos porque", T, t(20 * MINUTE + 36));
        assertDuration("siete días, 21 horas y doce minutos para llegar", F, t(7 * DAY + 21 * HOUR + 12 * MINUTE));
        assertDuration("minuto, segundos y milisegundo, microsegundos nanosegundo test", T, t(MINUTE + 1, MILLIS + MICROS + 1));
        assertDuration("5 ns ns", F, t(0, 5));
        assertNoDuration("ms 5 ns ns", F);
    }

    @Test(timeout = 4000) // 1024 formatos + análisis tardan <2s, usar 4s de timeout para PCs lentos
    public void testPerformanceWithFormatter() {
        // TODO no hay fracciones de segundo aquí ya que el formateador no las soporta
        final java.time.Duration[] alternatives = {
                t(1), t(5 * MINUTE), t(2 * HOUR), t(16 * DAY), t(WEEK), t(3 * MONTH), t(5 * YEAR),
                t(1e8 * YEAR), t(17 * WEEK), t(45)
        };

        final ParserFormatter npf = new ParserFormatter(new SpanishFormatter(), null);
        for (int i = 0; i < (1 << alternatives.length); ++i) {
            java.time.Duration durationToTest = java.time.Duration.ZERO;
            for (int j = 0; j < alternatives.length; ++j) {
                if ((i & (1 << j)) != 0) {
                    durationToTest = durationToTest.plus(alternatives[j]);
                }
            }

            final String formatted = npf.niceDuration(new Duration(durationToTest)).get();
            final TokenStream ts = new TokenStream(tokenizer.tokenize(formatted));
            assertDuration(formatted, ts, F, durationToTest);
            assertTrue(ts.finished());
        }
    }
}
