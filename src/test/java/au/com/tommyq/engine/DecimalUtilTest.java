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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DecimalUtilTest {
    private static final double epsilon = 1e-6;

    @Test
    public void testDecimalConversion(){
        assertEquals(2.982, DecimalUtil.toDouble(2982, 3), epsilon);
        assertEquals(2983, DecimalUtil.fromDouble(2.983, 3));
        assertTrue(Double.isNaN(DecimalUtil.toDouble(Long.MIN_VALUE, 3)));
        assertEquals(DecimalUtil.MANTISSA_NULL_VAL, DecimalUtil.fromDouble(Double.NaN, 3));

        assertEquals(2.98, DecimalUtil.toDouble(298), epsilon);
        assertEquals(298, DecimalUtil.fromDouble(2.98));
    }

    @Test
    public void testDecimalFormat(){
        final double value = DecimalUtil.toDouble(2989);
        assertEquals("$29.89", DecimalUtil.format(value));
    }

}