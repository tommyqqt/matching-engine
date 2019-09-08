package au.com.tommyq.market.engine;

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