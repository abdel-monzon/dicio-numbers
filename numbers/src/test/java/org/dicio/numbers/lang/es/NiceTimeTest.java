package org.dicio.numbers.lang.es;

import org.dicio.numbers.ParserFormatter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalTime;

import static org.dicio.numbers.test.TestUtils.F;
import static org.dicio.numbers.test.TestUtils.T;
import static org.junit.Assert.assertEquals;

public class NiceTimeTest {

    private static ParserFormatter pf;

    @BeforeClass
    public static void setup() {
        pf = new ParserFormatter(new SpanishFormatter(), null);
    }

    @Test
    public void random() {
        final LocalTime dt = LocalTime.of(13, 22, 3);
        assertEquals("la una y veintidós", pf.niceTime(dt).get());
        assertEquals("la una y veintidós de la tarde", pf.niceTime(dt).showAmPm(T).get());
        assertEquals("trece veintidós", pf.niceTime(dt).use24Hour(T).get());
        assertEquals("trece veintidós", pf.niceTime(dt).use24Hour(T).showAmPm(T).get());
        assertEquals("1:22", pf.niceTime(dt).speech(F).get());
        assertEquals("1:22 PM", pf.niceTime(dt).speech(F).showAmPm(T).get());
        assertEquals("13:22", pf.niceTime(dt).speech(F).use24Hour(T).get());
        assertEquals("13:22", pf.niceTime(dt).speech(F).use24Hour(T).showAmPm(T).get());
    }

    @Test
    public void oClock() {
        final LocalTime dt = LocalTime.of(15, 0, 32);
        assertEquals("las tres en punto", pf.niceTime(dt).get());
        assertEquals("las tres en punto de la tarde", pf.niceTime(dt).showAmPm(T).get());
        assertEquals("las quince en punto", pf.niceTime(dt).use24Hour(T).get());
        assertEquals("las quince en punto", pf.niceTime(dt).use24Hour(T).showAmPm(T).get());
        assertEquals("3:00", pf.niceTime(dt).speech(F).get());
        assertEquals("3:00 PM", pf.niceTime(dt).speech(F).showAmPm(T).get());
        assertEquals("15:00", pf.niceTime(dt).speech(F).use24Hour(T).get());
        assertEquals("15:00", pf.niceTime(dt).speech(F).use24Hour(T).showAmPm(T).get());
    }

    @Test
    public void afterMidnight() {
        final LocalTime dt = LocalTime.of(0, 2, 9);
        assertEquals("las doce y dos", pf.niceTime(dt).get());
        assertEquals("las doce y dos de la madrugada", pf.niceTime(dt).showAmPm(T).get());
        assertEquals("cero cero dos", pf.niceTime(dt).use24Hour(T).get());
        assertEquals("cero cero dos", pf.niceTime(dt).use24Hour(T).showAmPm(T).get());
        assertEquals("12:02", pf.niceTime(dt).speech(F).get());
        assertEquals("12:02 AM", pf.niceTime(dt).speech(F).showAmPm(T).get());
        assertEquals("00:02", pf.niceTime(dt).speech(F).use24Hour(T).get());
        assertEquals("00:02", pf.niceTime(dt).speech(F).use24Hour(T).showAmPm(T).get());
    }

    @Test
    public void quarterPast() {
        final LocalTime dt = LocalTime.of(1, 15, 33);
        assertEquals("la una y cuarto", pf.niceTime(dt).get());
        assertEquals("la una y cuarto de la madrugada", pf.niceTime(dt).showAmPm(T).get());
        assertEquals("la una y quince", pf.niceTime(dt).use24Hour(T).get());
        assertEquals("la una y quince", pf.niceTime(dt).use24Hour(T).showAmPm(T).get());
        assertEquals("1:15", pf.niceTime(dt).speech(F).get());
        assertEquals("1:15 AM", pf.niceTime(dt).speech(F).showAmPm(T).get());
        assertEquals("01:15", pf.niceTime(dt).speech(F).use24Hour(T).get());
        assertEquals("01:15", pf.niceTime(dt).speech(F).use24Hour(T).showAmPm(T).get());
    }

    @Test
    public void half() {
        final LocalTime dt = LocalTime.of(12, 30, 59);
        assertEquals("las doce y media", pf.niceTime(dt).get());
        assertEquals("las doce y media del mediodía", pf.niceTime(dt).showAmPm(T).get());
        assertEquals("las doce y treinta", pf.niceTime(dt).use24Hour(T).get());
        assertEquals("las doce y treinta", pf.niceTime(dt).use24Hour(T).showAmPm(T).get());
        assertEquals("12:30", pf.niceTime(dt).speech(F).get());
        assertEquals("12:30 PM", pf.niceTime(dt).speech(F).showAmPm(T).get());
        assertEquals("12:30", pf.niceTime(dt).speech(F).use24Hour(T).get());
        assertEquals("12:30", pf.niceTime(dt).speech(F).use24Hour(T).showAmPm(T).get());
    }

    @Test
    public void quarterTo() {
        final LocalTime dt = LocalTime.of(23, 45, 7);
        assertEquals("las doce menos cuarto", pf.niceTime(dt).get());
        assertEquals("las doce menos cuarto de la noche", pf.niceTime(dt).showAmPm(T).get());
        assertEquals("las veintitrés cuarenta y cinco", pf.niceTime(dt).use24Hour(T).get());
        assertEquals("las veintitrés cuarenta y cinco", pf.niceTime(dt).use24Hour(T).showAmPm(T).get());
        assertEquals("11:45", pf.niceTime(dt).speech(F).get());
        assertEquals("11:45 PM", pf.niceTime(dt).speech(F).showAmPm(T).get());
        assertEquals("23:45", pf.niceTime(dt).speech(F).use24Hour(T).get());
        assertEquals("23:45", pf.niceTime(dt).speech(F).use24Hour(T).showAmPm(T).get());
    }

    @Test
    public void tenAm() {
        final LocalTime dt = LocalTime.of(10, 3, 44);
        assertEquals("las diez y tres", pf.niceTime(dt).get());
        assertEquals("las diez y tres de la mañana", pf.niceTime(dt).showAmPm(T).get());
        assertEquals("las diez y tres", pf.niceTime(dt).use24Hour(T).get());
        assertEquals("las diez y tres", pf.niceTime(dt).use24Hour(T).showAmPm(T).get());
        assertEquals("10:03", pf.niceTime(dt).speech(F).get());
        assertEquals("10:03 AM", pf.niceTime(dt).speech(F).showAmPm(T).get());
        assertEquals("10:03", pf.niceTime(dt).speech(F).use24Hour(T).get());
        assertEquals("10:03", pf.niceTime(dt).speech(F).use24Hour(T).showAmPm(T).get());
    }

    @Test
    public void tenPm() {
        final LocalTime dt = LocalTime.of(22, 3, 44);
        assertEquals("las diez y tres", pf.niceTime(dt).get());
        assertEquals("las diez y tres de la noche", pf.niceTime(dt).showAmPm(T).get());
        assertEquals("las veintidós y tres", pf.niceTime(dt).use24Hour(T).get());
        assertEquals("las veintidós y tres", pf.niceTime(dt).use24Hour(T).showAmPm(T).get());
        assertEquals("10:03", pf.niceTime(dt).speech(F).get());
        assertEquals("10:03 PM", pf.niceTime(dt).speech(F).showAmPm(T).get());
        assertEquals("22:03", pf.niceTime(dt).speech(F).use24Hour(T).get());
        assertEquals("22:03", pf.niceTime(dt).speech(F).use24Hour(T).showAmPm(T).get());
    }
    }
