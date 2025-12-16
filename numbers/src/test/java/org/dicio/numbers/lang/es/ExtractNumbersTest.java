package org.dicio.numbers.lang.es;

import org.dicio.numbers.ParserFormatter;
import org.dicio.numbers.parser.lexer.TokenStream;
import org.dicio.numbers.test.WithTokenizerTestBase;
import org.dicio.numbers.unit.Number;
import org.junit.Test;

import java.util.function.Function;

import static org.dicio.numbers.test.TestUtils.F;
import static org.dicio.numbers.test.TestUtils.T;
import static org.dicio.numbers.test.TestUtils.n;
import static org.dicio.numbers.test.TestUtils.numberDeduceType;
import static org.junit.Assert.*;

public class ExtractNumbersTest extends WithTokenizerTestBase {

    @Override
    public String configFolder() {
        return "config/es-es";
    }


    private void assertNumberFunction(final String s,
                                      final Number value,
                                      final int finalTokenStreamPosition,
                                      final Function<SpanishNumberExtractor, Number> numberFunction) {
        final TokenStream ts = new TokenStream(tokenizer.tokenize(s));
        final Number number = numberFunction.apply(new SpanishNumberExtractor(ts));
        assertEquals("valor incorrecto para la cadena " + s, value, number);
        assertEquals("posición final incorrecta del token para el número " + value, finalTokenStreamPosition,
                ts.position);
    }

    private void assertNumberFunctionNull(final String s,
                                          final Function<SpanishNumberExtractor, Number> numberFunction) {
        assertNumberFunction(s, null, 0, numberFunction);
    }

    private void assertNumberInteger(final String s, final boolean allowOrdinal, final double value, final boolean isOrdinal, final int finalTokenStreamPosition) {
        assertNumberFunction(s, numberDeduceType(value).withOrdinal(isOrdinal), finalTokenStreamPosition,
                (enp) -> enp.numberInteger(allowOrdinal));
    }

    private void assertNumberIntegerNull(final String s, final boolean allowOrdinal) {
        assertNumberFunctionNull(s, (enp) -> enp.numberInteger(allowOrdinal));
    }

    private void assertNumberPoint(final String s, final boolean allowOrdinal, final double value, final boolean isOrdinal, final int finalTokenStreamPosition) {
        assertNumberFunction(s, numberDeduceType(value).withOrdinal(isOrdinal),
                finalTokenStreamPosition, (enp) -> enp.numberPoint(allowOrdinal));
    }

    private void assertNumberPointNull(final String s, final boolean allowOrdinal) {
        assertNumberFunctionNull(s, (enp) -> enp.numberPoint(allowOrdinal));
    }

    private void assertNumberSignPoint(final String s, final boolean allowOrdinal, final double value, final boolean isOrdinal, final int finalTokenStreamPosition) {
        assertNumberFunction(s, numberDeduceType(value).withOrdinal(isOrdinal),
                finalTokenStreamPosition, (enp) -> enp.numberSignPoint(allowOrdinal));
    }

    private void assertNumberSignPointNull(final String s, final boolean allowOrdinal) {
        assertNumberFunctionNull(s, (enp) -> enp.numberSignPoint(allowOrdinal));
    }

    private void assertDivideByDenominatorIfPossible(final String s, final Number startingNumber, final Number value, final int finalTokenStreamPosition) {
        assertNumberFunction(s, value, finalTokenStreamPosition,
                (enp) -> enp.divideByDenominatorIfPossible(startingNumber));
    }

    @Test
    public void testNumberInteger() {
        assertNumberInteger("mil quinientos sesenta y cuatro trillones y un billardo y cien billones", F, 1501564100e12, F, 11);
        assertNumberInteger("veinticinco mil millones, ciento sesenta y cuatro millones, siete mil diecinueve", T, 25164007019L, F, 14);
        assertNumberInteger("veinticinco mil millones, ciento sesenta y cuatro millones, siete mil millones", T, 25164000000L, F, 10);
        assertNumberInteger("dos mil ciento noventa y uno", F, 2191, F, 6);
        assertNumberInteger("novecientos diez", T, 910, F, 3);
        assertNumberInteger("dos millones", F, 2000000, F, 2);
        assertNumberInteger("mil diez", T, 1010, F, 2);
        assertNumberInteger("1234567890123", F, 1234567890123L, F, 1);
        assertNumberInteger("654 y", T, 654, F, 1);
        assertNumberInteger("cien cuatro,", F, 104, F, 3);
        assertNumberInteger("nueve mil, tres millones", T, 9000, F, 2);
        assertNumberInteger("una de noche", F, 1, F, 1);
    }

    @Test
       public void testNumberIntegerOrdinal() {
        assertNumberInteger("mil quinientos sesenta y cuatro trillones, un billardo, cien billonésimo", T, 1501564100e12, T, 11);
        assertNumberInteger("mil quinientos sesenta y cuatro trillones, uno billardésimo, cien billonésimo", T, 1501564e15, T, 8);
        assertNumberInteger("mil quinientos sesenta y cuatro trillones, uno billardésimo, cien billonésimo", F, 1501564e18, F, 7);
        assertNumberInteger("veinticinco mil millones, ciento sesenta y nueve millones, siete mil diecinueveavo", T, 25169007019L, T, 13);
        assertNumberInteger("73 mil millones, veintitrés millonésimo, siete mil diecinueve", T, 73023000000L, T, 6);
        assertNumberInteger("ciento seis mil millones, veintiún millones, un billardésimo", T, 106021000000L, F, 7);
        assertNumberInteger("ciento seis mil millones, veintiún millones y un milésimo", F, 106021000001L, F, 10);
        assertNumberInteger("diecinueve centésimo", T, 1900, F, 2);
        assertNumberInteger("diecinueveavo", T, 19, T, 1);
        assertNumberInteger("dos mil unésimo", T, 2001, T, 3);
        assertNumberInteger("dos mil unésimo", F, 2000, F, 2);
        assertNumberInteger("mil novecientos sieteavo", T, 1907, T, 3);
        assertNumberInteger("trece dieciseisavo", F, 13, F, 1);
        assertNumberInteger("dieciséis treceavo", T, 16, F, 1);
        assertNumberInteger("543789ª", T, 543789, T, 2);
        assertNumberInteger("12°tempo", T, 12, T, 2);
        assertNumberInteger("75.483.543ª", T, 75483543, T, 6);
        assertNumberIntegerNull("2938°", F);
        assertNumberIntegerNull("102.321ª", F);
    }

    @Test
    public void testNumberIntegerThousandSeparator() {
        // independent of short/long scale and of ordinal mode
        assertNumberInteger("23.001", F, 23001, F, 3);
        assertNumberInteger("167,42", T, 167, F, 1);
        assertNumberInteger("1.234.023.054. hola", F, 1234023054, F, 7);
        assertNumberInteger("23.001. un 500", T, 23001, F, 3);
        assertNumberInteger("5.030,dos", F, 5030, F, 3);
        assertNumberInteger("67.104,23", F, 67104, F, 3);
    }

    @Test
    public void testNumberIntegerYear() {
        // independent of short/long scale and of ordinal mode
        assertNumberInteger("dieciocho veintiuno", T, 18, F, 1);
        assertNumberInteger("diecinueve 745", F, 19, F, 1);
        assertNumberInteger("diecinueve 25", F, 1925, F, 2);
        assertNumberInteger("19 veinticinco", F, 19, F, 1);
        assertNumberInteger("19 25", F, 19, F, 1);
        assertNumberInteger("diecinueveavo veinticinco", F, 19, T, 1);
        assertNumberInteger("diez 21", F, 1021, F, 2);
        assertNumberInteger("diecinueve cero 6 y dos", T, 1906, F, 3);
        assertNumberInteger("veinte-cero-cero", T, 2000, F, 5);
        assertNumberInteger("once cero 0", F, 1100, F, 3);
        assertNumberInteger("diecisiete 0 0", F, 1700, F, 3);
        assertNumberInteger("sesenta y cuatro cien", F, 6400, F, 5);
        assertNumberInteger("doscientos doce cien", T, 212, F, 4);
        assertNumberInteger("58 cien", F, 5800, F, 2);
        assertNumberInteger("diecinueve cien", F, 1900, F, 2);
        assertNumberInteger("dieciocho 1", T, 18, F, 1);
    }

    private int tokensInFormattedString(final String formatted) {
        int tokensInFormatted = 1;
        for (int j = 0; j < formatted.length(); ++j) {
            if (formatted.charAt(j) == ' ' || formatted.charAt(j) == ',') {
                ++tokensInFormatted;
            }
        }
        return tokensInFormatted;
    }

    @Test
    public void testNumberIntegerWithFormatter() {
        final ParserFormatter npf = new ParserFormatter(new SpanishFormatter(), null);
        for (int i = 0; i < 1100000000;) {
            if (i < 2200) {
                ++i; // probar todos los números de 0 a 2200 (¡también prueba años!)
            } else if (i < 1000000) {
                i += 1207;
            } else {
                i += 299527;
            }

            // no ordinal
            String formatted = npf.pronounceNumber(i).places(0).get();
            int tokensInFormatted = tokensInFormattedString(formatted);
            assertNumberInteger(formatted, T, i, F, tokensInFormatted);

            // ordinal
            formatted = npf.pronounceNumber(i).places(0).ordinal(T).get();
            tokensInFormatted = tokensInFormattedString(formatted);
            assertNumberInteger(formatted, T, i, T, tokensInFormatted);
        }
    }

    @Test(timeout = 2000) // 20000 formatos + análisis tardan <1s, usar 2s de timeout para PCs lentos
    public void testNumberIntegerPerformance() {
        final ParserFormatter npf = new ParserFormatter(new SpanishFormatter(), null);
        final long startingValue = 54378960497L;
        for (long i = startingValue; i < startingValue + 10000; ++i) {
            // sin ordinal
            String formatted = npf.pronounceNumber(i).places(0).get();
            int tokensInFormatted = tokensInFormattedString(formatted);
            assertNumberInteger(formatted, T, i, F, tokensInFormatted);

            // ordinal
            formatted = npf.pronounceNumber(i).places(0).ordinal(true).get();
            tokensInFormatted = tokensInFormattedString(formatted);
            assertNumberInteger(formatted, T, i, T, tokensInFormatted);
        }
    }

    @Test
    public void testNumberIntegerNull() {
        assertNumberIntegerNull("",                    T);
        assertNumberIntegerNull("hola qué tal",        F);
        assertNumberIntegerNull(", y",                 T);
        assertNumberIntegerNull("y dos",               F);
        assertNumberIntegerNull(", 123485 y",          T);
        assertNumberIntegerNull("y 123",               F);
        assertNumberIntegerNull(" mil ",               T);
    }

    @Test
    public void testNumberPoint() {
        assertNumberPoint("mil quinientos setenta y cuatro coma nueve uno dos cero", T, 1574.912, F, 10);
        assertNumberPoint("veintitrés coma cero uno cero dos tres, quinientos", F, 23.01023, F, 8);
        assertNumberPoint("quinientos nueve coma ocho cuatro cinco", F, 509.845, F, 7);
        assertNumberPoint("veintitrés mil coma dieciséis", T, 23000, F, 3);
        assertNumberPoint("dos punto tres cuatro",   T, 2.34,      F, 4);
        assertNumberPoint("3645,7183",               F, 3645.7183, F, 3);
        assertNumberPoint("veinticinco,2",           T, 25.2,      F, 4);
        assertNumberPoint("ochenta coma 6745",       F, 80.6745,   F, 3);
        assertNumberPoint("4 coma 67 45",            T, 4.67,      F, 3);
        assertNumberPoint("4000 coma 6 63",          F, 4000.6,    F, 3);
        assertNumberPoint("74567 coma seis",         T, 74567.6,   F, 3);
        assertNumberPoint("cero , 6 8 2 cero veinte", F, 0.682,    F, 6);
        assertNumberPoint("74567 coma seis",         T, 74567.6,   F, 3);
        assertNumberPoint("uno coma veinte",         T, 1,         F, 1);
    }

    @Test
    public void testNumberPointFraction() {
        assertNumberPoint("veintitrés mil, cien sesenta y cuatro sobre dieciséis", F, 1443754, F, 10);
        assertNumberPoint("dieciséis dividido veintitrés mil, cien sesenta y cuatro", T, 1.0 / 1443754.0, F, 10);
        assertNumberPoint("8 mil y, 192 dividido 4 mil 96 ocho",  F, 2,                 F, 9);
        assertNumberPoint("nueve mil ocho cien / cien",           T, 98,                F, 6);
        assertNumberPoint("veinticuatro dividido sesenta y cinco", T, 24.0 / 65.0,       F, 5);
        assertNumberPoint("uno dividido cinco y medio",            F, 1.0 / 5.0,         F, 3);
        assertNumberPoint("veintiséis dividido siete",             T, 26.0 / 7.0,        F, 4);
        assertNumberPoint("47328 dividido 12093",                  F, 47328.0 / 12093.0, F, 3);
        assertNumberPoint("cinco / seis nueve dos",                T, 5.0 / 6.0,         F, 3);
        assertNumberPoint("nueve dividido, dos",                   F, 9,                 F, 1);
        assertNumberPoint("ocho dividido cinco",                   T, 8.0 / 5.0,         F, 3);
        assertNumberPoint("seis por diecinueve",                   F, 6,                 F, 1);
    }

    @Test
    public void testNumberPointOrdinal() {
        assertNumberPoint("quinto coma seis",                      T, 5,     T, 1);
        assertNumberPoint("3 mil 7 cientos decimo dividido seis", T, 3710,  T, 5);
        assertNumberPoint("3 mil 7 cientos decimo dividido seis", F, 3700,  F, 4);
        assertNumberPoint("ocho coma uno segundo",                F, 8.1,   F, 3);
        assertNumberPoint("ocho coma uno tercero",                T, 8.1,   F, 3);
        assertNumberPoint("ocho coma un tercero",                 T, 8,     F, 1);
        assertNumberPoint("seis dividido quinto",                 T, 6,     F, 1);
        assertNumberPoint("nove / treinta novenos",               T, 0.3,   F, 3);
        assertNumberPoint("nove / treinta novenos",               F, 0.3,   F, 3);
        assertNumberPoint("trece coma 1 2 3 °",                   T, 13.12, F, 4);
    }

    @Test
    public void testNumberPointNull() {
        assertNumberPointNull("",                     F);
        assertNumberPointNull("hola mundo",           T);
        assertNumberPointNull("coma",                 F);
        assertNumberPointNull(", veinte",             T);
        assertNumberPointNull(",9",                   T);
        assertNumberPointNull(",, 1 2 3 4",           F);
        assertNumberPointNull(", y seis cuatro ocho", T);
        assertNumberPointNull("dividido dos",         F);
        assertNumberPointNull(" uno dividido cinco",  T);
        assertNumberPointNull("coma 800",             F);
        assertNumberPointNull("punto 9",              F);
        assertNumberPointNull(",1",                   F);
    }

    @Test
    public void testNumberSignPoint() {
        assertNumberSignPoint("menos setenta y seis mil trescientos cincuenta y seis dividido 23", T, -76356.0 / 23.0, F, 10);
        assertNumberSignPoint("menos doce",               F, -12,      F, 2);
        assertNumberSignPoint("más millones",             T, 1000000,  F, 2);
        assertNumberSignPoint("más mil",                  T, 1000,     F, 2);
        assertNumberSignPoint("-1843",                    F, -1843,    F, 2);
        assertNumberSignPoint("+573,976",                 T, 573976,   F, 4);
        assertNumberSignPoint("menos 42903,5",            F, -42903.5, F, 4);
    }

    @Test
    public void testNumberSignPointOrdinal() {
        assertNumberSignPoint("menos doceavo",       T, -12,      T, 2);
        assertNumberSignPoint("-un centesimo",       F, -1,       F, 2);
        assertNumberSignPoint("más millionésimo diez", T, 1000000,  T, 2);
        assertNumberSignPoint("-1843ª",              T, -1843,    T, 3);
        assertNumberSignPoint("+573,976°",           T, 573976,   T, 5);
        assertNumberSignPointNull("menos primero", F);
        assertNumberSignPointNull("-1843ª",        F);
    }

    @Test
    public void testNumberSignPointNull() {
        assertNumberSignPointNull("",                          F);
        assertNumberSignPointNull("hola qué tal",            T);
        assertNumberSignPointNull("menos menos ciento sesenta",F);
        assertNumberSignPointNull(" más un millón",          T);
        assertNumberSignPointNull("+- 5",                    F);
        assertNumberSignPointNull("menos coma cero cuatro",  T);
    }

    @Test
    public void testDivideByDenominatorIfPossible() {
        assertDivideByDenominatorIfPossible("quintos",      n(5, F),    n(1, F),   1);
        assertDivideByDenominatorIfPossible("docena dos",   n(3, F),    n(36, F),  1);
        assertDivideByDenominatorIfPossible("medio y",      n(19, F),   n(9.5, F), 1);
        assertDivideByDenominatorIfPossible("%",            n(50, F),   n(0.5, F), 1);
        assertDivideByDenominatorIfPossible("‰",            n(1000, F), n(1, F),   1);
        assertDivideByDenominatorIfPossible("cuarto",       n(16, F),   n(4, F),   1);
        assertDivideByDenominatorIfPossible("cuarto",       n(4.4, F),  n(4.4, F), 0);
        assertDivideByDenominatorIfPossible("personas",     n(98, F),   n(98, F),  0);
    }
    }
                          
