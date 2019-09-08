package au.com.tommyq.market.engine;

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
