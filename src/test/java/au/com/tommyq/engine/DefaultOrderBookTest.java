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

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DefaultOrderBookTest {

    private DefaultOrderBook orderBook;

    private List<? super Event> outputEvents = new ArrayList<>();

    @After
    public void tearDown()  {
        outputEvents.clear();
    }

    @Test
    public void testRestingOrders(){
        orderBook = new DefaultOrderBook("EURUSD", DecimalUtil.fromDouble(1.07), DecimalUtil.fromDouble(2.2), outputEvents::add);
        final MutableOrder bid1 = new MutableOrder("AbcCapital", "EURUSD", Side.BID, 1, DecimalUtil.fromDouble(1.2));
        final MutableOrder bid2 = new MutableOrder("AbcCapital", "EURUSD", Side.BID, 2, DecimalUtil.fromDouble(1.3));
        final MutableOrder bid3 = new MutableOrder("AbcCapital", "EURUSD", Side.BID, 5, DecimalUtil.fromDouble(1.3));
        final MutableOrder offer1 = new MutableOrder("AbcCapital", "EURUSD", Side.OFFER, 2, DecimalUtil.fromDouble(1.5));
        final MutableOrder offer2 = new MutableOrder("AbcCapital", "EURUSD", Side.OFFER, 17, DecimalUtil.fromDouble(1.5));
        final MutableOrder offer3 = new MutableOrder("AbcCapital", "EURUSD", Side.OFFER, 16, DecimalUtil.fromDouble(1.6));
        orderBook.processOrder(bid1);
        orderBook.processOrder(bid2);
        orderBook.processOrder(bid3);
        orderBook.processOrder(offer1);
        orderBook.processOrder(offer2);
        orderBook.processOrder(offer3);
        final TopOfBookReport report = orderBook.topOfBookRpt();
        assertEquals(130, report.bestBid());
        assertEquals(7, report.bestBidQuantity());
        assertEquals(150, report.bestOffer());
        assertEquals(19, report.bestOfferQuantity());
    }

    @Test
    public void testFASOrders(){
        orderBook = new DefaultOrderBook("GBPEUR", DecimalUtil.fromDouble(1.05), DecimalUtil.fromDouble(2.2), outputEvents::add);
        final MutableOrder bid1 = new MutableOrder("AbcCapital", "GBPEUR", Side.BID, 1, DecimalUtil.fromDouble(1.1));
        final MutableOrder bid2 = new MutableOrder("AbcCapital", "GBPEUR", Side.BID, 2, DecimalUtil.fromDouble(1.2));
        final MutableOrder bid3 = new MutableOrder("AbcCapital", "GBPEUR", Side.BID, 2, DecimalUtil.fromDouble(1.3));
        final MutableOrder bid4 = new MutableOrder("AbcCapital", "GBPEUR", Side.BID, 5, DecimalUtil.fromDouble(1.3));
        final MutableOrder offer1 = new MutableOrder("AbcCapital", "GBPEUR", Side.OFFER, 2, DecimalUtil.fromDouble(1.5));
        final MutableOrder offer2 = new MutableOrder("AbcCapital", "GBPEUR", Side.OFFER, 17, DecimalUtil.fromDouble(1.5));
        final MutableOrder offer3 = new MutableOrder("AbcCapital", "GBPEUR", Side.OFFER, 16, DecimalUtil.fromDouble(1.6));
        orderBook.processOrder(bid1);
        orderBook.processOrder(bid2);
        orderBook.processOrder(bid3);
        orderBook.processOrder(bid4);
        orderBook.processOrder(offer1);
        orderBook.processOrder(offer2);
        orderBook.processOrder(offer3);
        final TopOfBookReport report = orderBook.topOfBookRpt();
        assertEquals(130, report.bestBid());
        assertEquals(7, report.bestBidQuantity());
        assertEquals(150, report.bestOffer());
        assertEquals(19, report.bestOfferQuantity());

        final MutableOrder offer4 = new MutableOrder("AbcCapital", "GBPEUR", Side.OFFER, 11, DecimalUtil.fromDouble(1.15));
        orderBook.processOrder(offer4);
        final TopOfBookReport report2 = orderBook.topOfBookRpt();
        assertEquals(110, report2.bestBid());
        assertEquals(1, report2.bestBidQuantity());
        assertEquals(115, report2.bestOffer());
        assertEquals(2, report2.bestOfferQuantity());

        final MutableOrder bid5 = new MutableOrder("AbcCapital", "GBPEUR", Side.BID, 24, DecimalUtil.fromDouble(1.55));
        orderBook.processOrder(bid5);
        final TopOfBookReport report3 = orderBook.topOfBookRpt();
        assertEquals(155, report3.bestBid());
        assertEquals(3, report3.bestBidQuantity());
        assertEquals(160, report3.bestOffer());
        assertEquals(16, report3.bestOfferQuantity());
    }

    @Test
    public void testFilledOrders(){
        orderBook = new DefaultOrderBook("GBPEUR", DecimalUtil.fromDouble(1.05), DecimalUtil.fromDouble(2.2), outputEvents::add);
        final MutableOrder bid1 = new MutableOrder("AbcCapital", "GBPEUR", Side.BID, 1, DecimalUtil.fromDouble(1.1));
        final MutableOrder bid2 = new MutableOrder("AbcCapital", "GBPEUR", Side.BID, 2, DecimalUtil.fromDouble(1.2));
        final MutableOrder bid3 = new MutableOrder("AbcCapital", "GBPEUR", Side.BID, 2, DecimalUtil.fromDouble(1.3));
        final MutableOrder bid4 = new MutableOrder("AbcCapital", "GBPEUR", Side.BID, 5, DecimalUtil.fromDouble(1.3));
        final MutableOrder offer1 = new MutableOrder("AbcCapital", "GBPEUR", Side.OFFER, 2, DecimalUtil.fromDouble(1.5));
        final MutableOrder offer2 = new MutableOrder("AbcCapital", "GBPEUR", Side.OFFER, 17, DecimalUtil.fromDouble(1.5));
        final MutableOrder offer3 = new MutableOrder("AbcCapital", "GBPEUR", Side.OFFER, 16, DecimalUtil.fromDouble(1.6));
        orderBook.processOrder(bid1);
        orderBook.processOrder(bid2);
        orderBook.processOrder(bid3);
        orderBook.processOrder(bid4);
        orderBook.processOrder(offer1);
        orderBook.processOrder(offer2);
        orderBook.processOrder(offer3);
        final TopOfBookReport report = orderBook.topOfBookRpt();
        assertEquals(130, report.bestBid());
        assertEquals(7, report.bestBidQuantity());
        assertEquals(150, report.bestOffer());
        assertEquals(19, report.bestOfferQuantity());

        final MutableOrder offer4 = new MutableOrder("AbcCapital", "GBPEUR", Side.OFFER, 6, DecimalUtil.fromDouble(1.3));
        orderBook.processOrder(offer4);
        final TopOfBookReport report2 = orderBook.topOfBookRpt();
        assertEquals(130, report2.bestBid());
        assertEquals(1, report2.bestBidQuantity());
        assertEquals(150, report2.bestOffer());
        assertEquals(19, report2.bestOfferQuantity());

        final MutableOrder bid5 = new MutableOrder("AbcCapital", "GBPEUR", Side.BID, 12, DecimalUtil.fromDouble(1.51));
        orderBook.processOrder(bid5);
        final TopOfBookReport report3 = orderBook.topOfBookRpt();
        assertEquals(130, report3.bestBid());
        assertEquals(1, report3.bestBidQuantity());
        assertEquals(150, report3.bestOffer());
        assertEquals(7, report3.bestOfferQuantity());
    }

    @Test
    public void testRejectOrder(){
        orderBook = new DefaultOrderBook("USDCAD", DecimalUtil.fromDouble(2.95), DecimalUtil.fromDouble(3.05), outputEvents::add);
        final MutableOrder bigOrder = new MutableOrder("AbcCapital", "USDCAD", Side.OFFER, 10, DecimalUtil.fromDouble(5));
        final MutableOrder smallOrder = new MutableOrder("AbcCapital", "USDCAD", Side.OFFER, 10, DecimalUtil.fromDouble(2.8));
        final MutableOrder noSideOrder = new MutableOrder("AbcCapital", "USDCAD", Side.NULL_VAL, 10, DecimalUtil.fromDouble(2.96));
        orderBook.processOrder(bigOrder);
        orderBook.processOrder(smallOrder);
        orderBook.processOrder(noSideOrder);
        final int eventsSize = outputEvents.size();
        assertEquals(3, eventsSize);
        for(Object execRpt : outputEvents){
            Assert.assertEquals(OrdStatus.REJECTED, ((ExecutionReport)execRpt).ordStatus());
        }
    }

    @Test
    public void testSampleOrders() throws Exception{
        long minPrice = DecimalUtil.fromDouble(2.95);
        long maxPrice = DecimalUtil.fromDouble(3.05);
        orderBook = new DefaultOrderBook("USDCAD", minPrice, maxPrice, outputEvents::add);
        final MutableOrder order1 = new MutableOrder("AbcCapital", "USDCAD", Side.OFFER, 10, DecimalUtil.fromDouble(2.99));
        final MutableOrder order2 = new MutableOrder("SuperInvestment", "USDCAD", Side.BID, 5, DecimalUtil.fromDouble(2.98));
        orderBook.processOrder(order1);
        orderBook.processOrder(order2);
        final TopOfBookReport report = orderBook.topOfBookRpt();
        assertEquals(298, report.bestBid());
        assertEquals(5, report.bestBidQuantity());
        assertEquals(299, report.bestOffer());
        assertEquals(10, report.bestOfferQuantity());

        final MutableOrder order3 = new MutableOrder("OneBank", "USDCAD", Side.BID, 10, DecimalUtil.fromDouble(2.97));
        final MutableOrder order4 = new MutableOrder("TravelExchanger", "USDCAD", Side.OFFER, 7, DecimalUtil.fromDouble(2.97));
        orderBook.processOrder(order3);
        orderBook.processOrder(order4);
        final TopOfBookReport report2 = orderBook.topOfBookRpt();
        assertEquals(297, report2.bestBid());
        assertEquals(8, report2.bestBidQuantity());
        assertEquals(299, report2.bestOffer());
        assertEquals(10, report2.bestOfferQuantity());

        final int eventsSize = outputEvents.size();
        assertEquals(2, eventsSize);
        ExecutionReport execRpt = (ExecutionReport)outputEvents.get(0);
        assertEquals("TravelExchanger", execRpt.user());
        assertEquals("SuperInvestment", execRpt.cpty());
        assertEquals("USDCAD", execRpt.instrument());
        assertEquals(5, execRpt.quantity());

        execRpt = (ExecutionReport)outputEvents.get(1);
        assertEquals("TravelExchanger", execRpt.user());
        assertEquals("OneBank", execRpt.cpty());
        assertEquals("USDCAD", execRpt.instrument());
        assertEquals(2, execRpt.quantity());
    }

}