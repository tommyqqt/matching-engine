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

package au.com.tommyq;

import au.com.tommyq.engine.DefaultPriceLevel;
import au.com.tommyq.engine.MutableOrder;
import au.com.tommyq.engine.PriceLevel;
import au.com.tommyq.engine.Side;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReportingTest {

    @Test
    public void testFormatPriceLevel() {
        final DefaultPriceLevel level = new DefaultPriceLevel(76, 0, 20);
        level.addOrder(new MutableOrder("testUser1", "AUDUSD", Side.OFFER, 1, 76));
        level.addOrder(new MutableOrder("testUser1", "AUDUSD", Side.OFFER, 50, 76));
        level.addOrder(new MutableOrder("testUser1", "AUDUSD", Side.OFFER, 100, 76));

        final DefaultPriceLevel level2 = new DefaultPriceLevel(75, 0, 20);
        level2.addOrder(new MutableOrder("testUser2", "AUDUSD", Side.OFFER, 250, 75));
        level2.addOrder(new MutableOrder("testUser2", "AUDUSD", Side.OFFER, 12, 75));
        level2.addOrder(new MutableOrder("testUser2", "AUDUSD", Side.OFFER, 8, 75));

        System.out.println("Price   Quantity  | Orders");
        System.out.println(Reporting.formatPriceLevel(level));
        System.out.println(Reporting.formatPriceLevel(level2));
    }
}