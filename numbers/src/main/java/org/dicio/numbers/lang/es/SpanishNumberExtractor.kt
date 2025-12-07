package org.dicio.numbers.lang.es

import org.dicio.numbers.parser.lexer.TokenStream
import org.dicio.numbers.unit.Number
import org.dicio.numbers.util.NumberExtractorUtils

class SpanishNumberExtractor internal constructor(private val ts: TokenStream) {
    fun numberPreferOrdinal(): Number? {
        var number = numberSuffixMultiplier()
        if (number == null) {
            number = numberSignPoint(true)
        }

        if (number != null) {
            number = divideByDenominatorIfPossible(number)
        }
        return number
    }

    fun numberPreferFraction(): Number? {
        var number = numberSuffixMultiplier()
        if (number == null) {
            number = numberSignPoint(false)
        }

        number = if (number == null) {
            numberSignPoint(true)
        } else {
            divideByDenominatorIfPossible(number)
        }
        return number
    }

    fun numberNoOrdinal(): Number? {
        var number = numberSuffixMultiplier()
        if (number == null) {
            number = numberSignPoint(false)
        }

        if (number != null) {
            number = divideByDenominatorIfPossible(number)
        }

        return number
    }

    fun divideByDenominatorIfPossible(numberToEdit: Number): Number? {
        if (!numberToEdit.isOrdinal && !numberToEdit.isDecimal
            && !ts[0].hasCategory("ignore")
        ) {
            val originalPosition = ts.position
            val denominator = numberInteger(true)
            if (denominator == null) {
                if (ts[0].hasCategory("suffix_multiplier")) {
                    ts.movePositionForwardBy(1)

                    val multiplier = ts[-1].number
                    if (multiplier?.isDecimal == true &&
                        (1 / multiplier.decimalValue()).toLong().toDouble()
                        == (1 / multiplier.decimalValue())
                    ) {
                        return numberToEdit.divide((1 / multiplier.decimalValue()).toLong())
                    }

                    return numberToEdit.multiply(multiplier)
                }
            } else if (denominator.isOrdinal && denominator.moreThan(2)) {
                return numberToEdit.divide(denominator)
            } else {
                ts.position = originalPosition
            }
        }
        return numberToEdit
    }

    fun numberSuffixMultiplier(): Number? {
        if (ts[0].hasCategory("suffix_multiplier")) {
            ts.movePositionForwardBy(1)
            return ts[-1].number
        } else {
            return null
        }
    }

    fun numberSignPoint(allowOrdinal: Boolean): Number? {
        return NumberExtractorUtils.signBeforeNumber(ts) { numberPoint(allowOrdinal) }
    }

    fun numberPoint(allowOrdinal: Boolean): Number? {
        var n = numberInteger(allowOrdinal).let {
            if (it == null || it.isOrdinal) {
                return@numberPoint it
            }
            it
        }

        if (ts[0].hasCategory("point")) {
            if (!ts[1].hasCategory("digit_after_point")
                && (!NumberExtractorUtils.isRawNumber(ts[1]) || ts[2].hasCategory("ordinal_suffix"))
            ) {
                return n
            }
            ts.movePositionForwardBy(1)

            var magnitude = 0.1
            if (ts[0].value.length > 1 && NumberExtractorUtils.isRawNumber(ts[0])) {
                for (i in 0 until ts[0].value.length) {
                    n = n.plus((ts[0].value[i].code - '0'.code) * magnitude)
                    magnitude /= 10.0
                }
                ts.movePositionForwardBy(1)
            } else {
                while (true) {
                    if (ts[0].hasCategory("digit_after_point")
                        || (ts[0].value.length == 1 && NumberExtractorUtils.isRawNumber(ts[0])
                                && !ts[1].hasCategory("ordinal_suffix"))
                    ) {
                        n = n.plus(ts[0].number!!.multiply(magnitude))
                        magnitude /= 10.0
                    } else {
                        break
                    }
                    ts.movePositionForwardBy(1)
                }
            }
        } else if (ts[0].hasCategory("fraction_separator")) {
            var separatorLength = 1
            if (ts[1].hasCategory("fraction_separator_secondary")) {
                separatorLength = 2
            }

            ts.movePositionForwardBy(separatorLength)
            val denominator = numberInteger(false)
            if (denominator == null) {
                ts.movePositionForwardBy(-separatorLength)
            } else {
                return n.divide(denominator)
            }
        }

        return n
    }

    fun numberInteger(allowOrdinal: Boolean): Number? {
        if (ts[0].hasCategory("ignore")) {
            return null
        }

        var n = NumberExtractorUtils.numberMadeOfGroups(
            ts,
            allowOrdinal,
            NumberExtractorUtils::numberGroupShortScale
        )
        if (n == null) {
            return NumberExtractorUtils.numberBigRaw(
                ts,
                allowOrdinal
            )
        } else if (n.isOrdinal) {
            return n
        }

        if (n.lessThan(1000)) {
            if (NumberExtractorUtils.isRawNumber(ts[-1]) && ts[0].hasCategory("thousand_separator") && ts[1].value.length == 3 && NumberExtractorUtils.isRawNumber(
                    ts[1]
                )
            ) {
                val originalPosition = ts.position - 1

                while (ts[0].hasCategory("thousand_separator") && ts[1].value.length == 3 && NumberExtractorUtils.isRawNumber(
                        ts[1]
                    )
                ) {
                    n = n!!.multiply(1000).plus(ts[1].number)
                    ts.movePositionForwardBy(2)
                }

                if (ts[0].hasCategory("ordinal_suffix")) {
                    if (allowOrdinal) {
                        ts.movePositionForwardBy(1)
                        return n!!.withOrdinal(true)
                    } else {
                        ts.position = originalPosition
                        return null
                    }
                }
            }
        }

        return n
    }
}
