/*
 * MIT License
 *
 * Copyright (c) 2019 tommyqqt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package au.com.tommyq.engine;

import java.text.DecimalFormat;

/**
 * Naive Utility for
 * double from/to long conversion given a precision.
 * Ignore rounding
 * Default precision = 2
 */
public class DecimalUtil {

    public static final long MANTISSA_NULL_VAL = Long.MIN_VALUE;

    private static final int DEFAULT_PRECISION = 2;

    private static final DecimalFormat df = new DecimalFormat("'$'0.00");

    public static String format(final double value){
        if(Double.isNaN(value)){
            return "NaN";
        }
        return df.format(value);
    }

    public static long fromDouble(final double value, final int precision){
        return !Double.isNaN(value) ? Math.round(value * Math.pow(10, precision)) : MANTISSA_NULL_VAL;
    }

    public static double toDouble(final long mantissa, final int precision){
        return mantissa != MANTISSA_NULL_VAL ? mantissa * Math.pow(10, -precision) : Double.NaN;
    }

    public static final long fromDouble(final double value){
        return fromDouble(value, DEFAULT_PRECISION);
    }

    public static final double toDouble(final long mantissa){
        return toDouble(mantissa, DEFAULT_PRECISION);
    }
}
