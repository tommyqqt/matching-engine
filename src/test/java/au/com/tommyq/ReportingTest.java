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

import au.com.tommyq.engine.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReportingTest {

    //@Test
    public void testFormatBookSnapshot() {
        final DefaultPriceLevel level = new DefaultPriceLevel(76, 0, 20);
        level.addOrder(new MutableOrder("testUser1", "AUDUSD", Side.OFFER, 1, 76));
        level.addOrder(new MutableOrder("testUser1", "AUDUSD", Side.OFFER, 50, 76));
        level.addOrder(new MutableOrder("testUser1", "AUDUSD", Side.OFFER, 100, 76));

        final DefaultPriceLevel level2 = new DefaultPriceLevel(78, 0, 20);
        level2.addOrder(new MutableOrder("testUser2", "AUDUSD", Side.OFFER, 250, 78));
        level2.addOrder(new MutableOrder("testUser2", "AUDUSD", Side.OFFER, 12, 78));
        level2.addOrder(new MutableOrder("testUser2", "AUDUSD", Side.OFFER, 8, 78));

        final PriceLevel[] depth = new PriceLevel[6];
        depth[3] = level;
        depth[2] = level2;
        final String expected = "                ArrayIndex  Price   Quantity  | Orders\n" +
                "                5            \n" +
                "                4            \n" +
                "Best OFFER -->  3           $0.76   151       | 1     50    100   \n" +
                "Best BID   -->  2           $0.78   270       | 250   12    8     \n" +
                "                1            \n" +
                "                0            \n";

        final OrderBookSnapshot snapshot = new OrderBookSnapshot("AUDUSD", depth, depth.length/2 - 1, depth.length/2);
        assertEquals(expected, Reporting.reportBookSnapshot(snapshot));
    }

}