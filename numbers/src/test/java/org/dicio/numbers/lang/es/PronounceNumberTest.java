package org.dicio.numbers.lang.es;

import org.dicio.numbers.ParserFormatter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.dicio.numbers.test.TestUtils.F;
import static org.dicio.numbers.test.TestUtils.T;
import static org.junit.Assert.assertEquals;

public class PronounceNumberTest {

    private static ParserFormatter pf;

    @BeforeClass
    public static void setup() {
        pf = new ParserFormatter(new SpanishFormatter(), null);
    }

    @Test
    public void smallIntegers() {
        assertEquals("cero", pf.pronounceNumber(0).get());
        assertEquals("uno", pf.pronounceNumber(1).get());
        assertEquals("diez", pf.pronounceNumber(10).get());
        assertEquals("quince", pf.pronounceNumber(15).get());
        assertEquals("veinte", pf.pronounceNumber(20).get());
        assertEquals("veintisiete", pf.pronounceNumber(27).get());
        assertEquals("treinta", pf.pronounceNumber(30).get());
        assertEquals("treinta y tres", pf.pronounceNumber(33).get());
    }

    @Test
    public void negativeSmallIntegers() {
        assertEquals("menos uno", pf.pronounceNumber(-1).get());
        assertEquals("menos diez", pf.pronounceNumber(-10).get());
        assertEquals("menos quince", pf.pronounceNumber(-15).get());
        assertEquals("menos veinte", pf.pronounceNumber(-20).get());
        assertEquals("menos veintisiete", pf.pronounceNumber(-27).get());
        assertEquals("menos treinta", pf.pronounceNumber(-30).get());
        assertEquals("menos treinta y tres", pf.pronounceNumber(-33).get());
    }

    @Test
    public void decimals() {
        assertEquals("cero punto cero cinco", pf.pronounceNumber(0.05).get());
        assertEquals("menos cero punto cero cinco", pf.pronounceNumber(-0.05).get());
        assertEquals("uno punto dos tres", pf.pronounceNumber(1.234).get());
        assertEquals("veintiuno punto dos seis cuatro", pf.pronounceNumber(21.264).places(5).get());
        assertEquals("veintiuno punto dos seis cuatro", pf.pronounceNumber(21.264).places(4).get());
        assertEquals("veintiuno punto dos seis cuatro", pf.pronounceNumber(21.264).places(3).get());
        assertEquals("veintiuno punto dos seis", pf.pronounceNumber(21.264).places(2).get());
        assertEquals("veintiuno punto tres", pf.pronounceNumber(21.264).places(1).get());
        assertEquals("veintiuno", pf.pronounceNumber(21.264).places(0).get());
        assertEquals("menos veintiuno punto dos seis cuatro", pf.pronounceNumber(-21.264).places(5).get());
        assertEquals("menos veintiuno punto dos seis cuatro", pf.pronounceNumber(-21.264).places(4).get());
        assertEquals("menos veintiuno punto dos seis cuatro", pf.pronounceNumber(-21.264).places(3).get());
        assertEquals("menos veintiuno punto dos seis", pf.pronounceNumber(-21.264).places(2).get());
        assertEquals("menos veintiuno punto tres", pf.pronounceNumber(-21.264).places(1).get());
        assertEquals("menos veintiuno", pf.pronounceNumber(-21.264).places(0).get());
    }

    @Test
    public void roundingDecimals() {
        assertEquals("cero", pf.pronounceNumber(0.05).places(0).get());
        assertEquals("cero", pf.pronounceNumber(-0.4).places(0).get());
        assertEquals("menos veintidos", pf.pronounceNumber(-21.7).places(0).get());
        assertEquals("ochenta y nueve", pf.pronounceNumber(89.2).places(0).get());
        assertEquals("noventa", pf.pronounceNumber(89.9).places(0).get());
        assertEquals("menos uno", pf.pronounceNumber(-0.5).places(0).get());
        assertEquals("cero", pf.pronounceNumber(-0.4).places(0).get());
        assertEquals("seis punto tres", pf.pronounceNumber(6.28).places(1).get());
        assertEquals("menos tres punto uno", pf.pronounceNumber(-3.14).places(1).get());
        // note: 3.15 does not yield "tres punto dos" because of floating point errors
        assertEquals("tres punto dos", pf.pronounceNumber(3.150001).places(1).get());
        assertEquals("cero punto tres", pf.pronounceNumber(0.25).places(1).get());
        assertEquals("menos cero punto tres", pf.pronounceNumber(-0.25).places(1).get());
        assertEquals("diecinueve", pf.pronounceNumber(19.004).get());
    }

    @Test
    public void ciento() {
        assertEquals("cien", pf.pronounceNumber(100).get());
        assertEquals("seiscientos setenta y ocho", pf.pronounceNumber(678).get());

        assertEquals("ciento tres millones, doscientos cincuenta y cuatro mil, seiscientos cincuenta y cuatro",
                pf.pronounceNumber(103254654).get());
        assertEquals("un millón, quinientos doce mil, cuatrocientos cincuenta y siete",
                pf.pronounceNumber(1512457).get());
        assertEquals("doscientos nueve mil, novecientos noventa y seis",
                pf.pronounceNumber(209996).get());
    }

    @Test
    public void year() {
        assertEquals("mil cuatrocientos cincuenta y seis", pf.pronounceNumber(1456).get());
        assertEquals("mil novecientos ochenta y cuatro", pf.pronounceNumber(1984).get());
        assertEquals("mil ochocientos uno", pf.pronounceNumber(1801).get());
        assertEquals("mil cien", pf.pronounceNumber(1100).get());
        assertEquals("mil doscientos uno", pf.pronounceNumber(1201).get());
        assertEquals("mil quinientos diez", pf.pronounceNumber(1510).get());
        assertEquals("mil seis", pf.pronounceNumber(1006).get());
        assertEquals("mil", pf.pronounceNumber(1000).get());
        assertEquals("dos mil", pf.pronounceNumber(2000).get());
        assertEquals("dos mil quince", pf.pronounceNumber(2015).get());
        assertEquals("cuatro mil ochocientos veintisiete", pf.pronounceNumber(4827).get());
    }

    @Test
    public void scientificNotation() {
        assertEquals("cero", pf.pronounceNumber(0.0).scientific(T).get());
        assertEquals("tres punto tres por diez a la uno",
                pf.pronounceNumber(33).scientific(T).get());
        assertEquals("dos punto nueve nueve por diez a la ocho",
                pf.pronounceNumber(299492458).scientific(T).get());
        assertEquals("dos punto nueve nueve siete nueve dos cinco por diez a la ocho",
                pf.pronounceNumber(299792458).scientific(T).places(6).get());
        assertEquals("uno punto seis siete dos por diez a la menos veintisiete",
                pf.pronounceNumber(1.672e-27).scientific(T).places(3).get());

        // auto scientific notation when number is too big to be pronounced
        assertEquals("dos punto nueve cinco por diez a la veinticuatro",
                pf.pronounceNumber(2.9489e24).get());
    }

    private void assertShortLongScale(final double number,
                                      final String shortScale,
                                      final String longScale) {
        assertEquals(shortScale, pf.pronounceNumber(number).shortScale(T).get());
        assertEquals(longScale, pf.pronounceNumber(number).shortScale(F).get());
    }

    @Test
    public void largeNumbers() {
        assertShortLongScale(1001892,
                "un millón, mil, ochocientos noventa y dos",
                "un millón, mil, ochocientos noventa y dos");
        assertShortLongScale(299792458,
                "doscientos noventa y nueve millones, setecientos noventa y dos mil, cuatrocientos cincuenta y ocho",
                "doscientos noventa y nueve millones, setecientos noventa y dos mil, cuatrocientos cincuenta y ocho");
        assertShortLongScale(-100202133440.0,
                "menos cien mil millones, doscientos dos millones, ciento treinta y tres mil, cuatrocientos cuarenta",
                "menos cien mil millones, doscientos dos millones, ciento treinta y tres mil, cuatrocientos cuarenta");
        assertShortLongScale(20102000987000.0,
                "veinte billones, ciento dos mil millones, novecientos ochenta y siete mil",
                "veinte billones, ciento dos mil millones, novecientos ochenta y siete mil");
        assertShortLongScale(-2061000560007060.0,
                "menos dos billones, sesenta y un mil millones, quinientos sesenta millones, siete mil, sesenta",
                "menos dos billones, sesenta y un mil millones, quinientos sesenta millones, siete mil, sesenta");
        assertShortLongScale(9111202032999999488.0, // floating point errors
                "nueve trillones, ciento once billones, veinte mil millones, trescientos veintinueve millones, novecientos noventa y nueve mil, cuatrocientos ochenta y ocho",
                "nueve trillones, ciento once billones, veinte mil millones, trescientos veintinueve millones, novecientos noventa y nueve mil, cuatrocientos ochenta y ocho");

        assertShortLongScale(29000.0, "veintinueve mil", "veintinueve mil");
        assertShortLongScale(301000.0, "trescientos uno mil", "trescientos uno mil");
        assertShortLongScale(4000000.0, "cuatro millones", "cuatro millones");
        assertShortLongScale(50000000.0, "cincuenta millones", "cincuenta millones");
        assertShortLongScale(630000000.0, "seiscientos treinta millones", "seiscientos treinta millones");
        assertShortLongScale(7000000000.0, "siete mil millones", "siete mil millones");
        assertShortLongScale(16000000000.0, "dieciséis mil millones", "dieciséis mil millones");
        assertShortLongScale(923000000000.0, "novecientos veintitrés mil millones", "novecientos veintitrés mil millones");
        assertShortLongScale(1000000000000.0, "un billón", "un billón");
        assertShortLongScale(29000000000000.0, "veintinueve billones", "veintinueve billones");
        assertShortLongScale(308000000000000.0, "trescientos ocho billones", "trescientos ocho billones");
        assertShortLongScale(4000000000000000.0, "cuatro mil billones", "cuatro mil billones");
        assertShortLongScale(52000000000000000.0, "cincuenta y dos mil billones", "cincuenta y dos mil billones");
        assertShortLongScale(640000000000000000.0, "seiscientos cuarenta mil billones", "seiscientos cuarenta mil billones");
        assertShortLongScale(7000000000000000000.0, "siete trillones", "siete trillones");

        // TODO maybe improve this
        assertShortLongScale(1000001, "un millón, uno", "un millón, uno");
        assertShortLongScale(-2000000029, "menos dos mil millones, veintinueve", "menos dos mil millones, veintinueve");
    }

    @Test
    public void ordinal() {
        // small numbers
        assertEquals("primero", pf.pronounceNumber(1).shortScale(T).ordinal(T).get());
        assertEquals("primero", pf.pronounceNumber(1).shortScale(F).ordinal(T).get());
        assertEquals("décimo", pf.pronounceNumber(10).shortScale(T).ordinal(T).get());
        assertEquals("décimo", pf.pronounceNumber(10).shortScale(F).ordinal(T).get());
        assertEquals("decimoquinto", pf.pronounceNumber(15).shortScale(T).ordinal(T).get());
        assertEquals("decimoquinto", pf.pronounceNumber(15).shortScale(F).ordinal(T).get());
        assertEquals("vigésimo", pf.pronounceNumber(20).shortScale(T).ordinal(T).get());
        assertEquals("vigésimo", pf.pronounceNumber(20).shortScale(F).ordinal(T).get());
        assertEquals("vigésimo séptimo", pf.pronounceNumber(27).shortScale(T).ordinal(T).get());
        assertEquals("vigésimo séptimo", pf.pronounceNumber(27).shortScale(F).ordinal(T).get());
        assertEquals("trigésimo", pf.pronounceNumber(30).shortScale(T).ordinal(T).get());
        assertEquals("trigésimo", pf.pronounceNumber(30).shortScale(F).ordinal(T).get());
        assertEquals("trigésimo tercero", pf.pronounceNumber(33).shortScale(T).ordinal(T).get());
        assertEquals("trigésimo tercero", pf.pronounceNumber(33).shortScale(F).ordinal(T).get());
        assertEquals("centésimo", pf.pronounceNumber(100).shortScale(T).ordinal(T).get());
        assertEquals("centésimo", pf.pronounceNumber(100).shortScale(F).ordinal(T).get());
        assertEquals("milésimo", pf.pronounceNumber(1000).shortScale(T).ordinal(T).get());
        assertEquals("milésimo", pf.pronounceNumber(1000).shortScale(F).ordinal(T).get());
        assertEquals("diez milésimo", pf.pronounceNumber(10000).shortScale(T).ordinal(T).get());
        assertEquals("diez milésimo", pf.pronounceNumber(10000).shortScale(F).ordinal(T).get());
        assertEquals("dos centésimo", pf.pronounceNumber(200).shortScale(T).ordinal(T).get());
        assertEquals("dos centésimo", pf.pronounceNumber(200).shortScale(F).ordinal(T).get());
        assertEquals("dieciocho mil, seiscientos noventa y primero", pf.pronounceNumber(18691).ordinal(T).shortScale(T).get());
        assertEquals("dieciocho mil, seiscientos noventa y primero", pf.pronounceNumber(18691).ordinal(T).shortScale(F).get());
        assertEquals("mil, quinientos sesenta y séptimo", pf.pronounceNumber(1567).ordinal(T).shortScale(T).get());
        assertEquals("mil, quinientos sesenta y séptimo", pf.pronounceNumber(1567).ordinal(T).shortScale(F).get());

        // big numbers
        assertEquals("millonésimo", pf.pronounceNumber(1000000).ordinal(T).get());
        assertEquals("dieciocho millonésimo", pf.pronounceNumber(18000000).ordinal(T).get());
        assertEquals("dieciocho millones, centésimo", pf.pronounceNumber(18000100).ordinal(T).get());
        assertEquals("ciento veintisiete mil millonésimo", pf.pronounceNumber(127000000000.0).ordinal(T).shortScale(T).get());
        assertEquals("doscientos uno mil millonésimo", pf.pronounceNumber(201000000000.0).ordinal(T).shortScale(F).get());
        assertEquals("novecientos trece mil millones, ochenta millones, seiscientos mil, sexagésimo cuarto", pf.pronounceNumber(913080600064.0).ordinal(T).shortScale(T).get());
        assertEquals("novecientos trece mil millones, ochenta millones, seiscientos mil, sexagésimo cuarto", pf.pronounceNumber(913080600064.0).ordinal(T).shortScale(F).get());
        assertEquals("un billón, dos millonésimo", pf.pronounceNumber(1000002000000.0).ordinal(T).shortScale(T).get());
        assertEquals("un billón, dos millonésimo", pf.pronounceNumber(1000002000000.0).ordinal(T).shortScale(F).get());
        assertEquals("cuatro billones, un millonésimo", pf.pronounceNumber(4000001000000.0).ordinal(T).shortScale(T).get());
        assertEquals("cuatro billones, un millonésimo", pf.pronounceNumber(4000001000000.0).ordinal(T).shortScale(F).get());

        // decimal numbers and scientific notation: the behaviour should be the same as with ordinal=F
        assertEquals("dos punto siete ocho", pf.pronounceNumber(2.78).ordinal(T).get());
        assertEquals("tercero", pf.pronounceNumber(2.78).places(0).ordinal(T).get());
        assertEquals("decimonoveno", pf.pronounceNumber(19.004).ordinal(T).get());
        assertEquals("ochocientos treinta millones, cuatrocientos treinta y ocho mil, noventa y dos punto uno ocho tres", pf.pronounceNumber(830438092.1829).places(3).ordinal(T).get());
        assertEquals("dos punto cinco cuatro por diez a la seis", pf.pronounceNumber(2.54e6).ordinal(T).scientific(T).get());
    }

    @Test
    public void edgeCases() {
        assertEquals("cero", pf.pronounceNumber(0.0).get());
        assertEquals("cero", pf.pronounceNumber(-0.0).get());
        assertEquals("infinito", pf.pronounceNumber(Double.POSITIVE_INFINITY).get());
        assertEquals("infinito negativo", pf.pronounceNumber(Double.NEGATIVE_INFINITY).scientific(F).get());
        assertEquals("infinito negativo", pf.pronounceNumber(Double.NEGATIVE_INFINITY).scientific(T).get());
        assertEquals("no es un número", pf.pronounceNumber(Double.NaN).get());
    }
}
