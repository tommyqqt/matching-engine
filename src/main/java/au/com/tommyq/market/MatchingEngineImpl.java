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

package au.com.tommyq.market;

import au.com.tommyq.market.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This implementation maintains an event loop that polls for or offers events to an event queue
 * Input events:  Orders and commands
 * Output events: Best bid/offer (top of book) report event
 *                Execution report event
 */
public class MatchingEngineImpl implements MatchingEngine {
    private static final Logger logger = LoggerFactory.getLogger(MatchingEngineImpl.class);
    private final List<InstrumentConfig> instruments;
    private Map<String, OrderBook> orderBooks;
    private final ExecutorService executorService;
    private final Consumer<String> consoleConsumer;

    private final Queue<? super Event> eventQueue = new ConcurrentLinkedQueue<>();

    public MatchingEngineImpl(final List<InstrumentConfig> instruments,
                              final ExecutorService executorService,
                              final Consumer<String> consoleConsumer) {
        this.instruments = Objects.requireNonNull(instruments);
        this.executorService = Objects.requireNonNull(executorService);
        this.consoleConsumer = consoleConsumer;
        configureOrderBooks(instruments);
    }

    private void configureOrderBooks(final List<InstrumentConfig> instruments) {
        orderBooks = instruments.stream().
                map(instrument -> new DefaultOrderBook(instrument.name(),
                        DecimalUtil.fromDouble(instrument.minPrice()),
                        DecimalUtil.fromDouble(instrument.maxPrice()), eventQueue::offer))
                .collect(Collectors.toMap(OrderBook::instrument, Function.identity()));
    }

    @Override
    public void placeBid(final String name, final String instrument, final int quantity, final double price) {
        final MutableOrder order = new MutableOrder(name, instrument, Side.BID, quantity, DecimalUtil.fromDouble(price));
        eventQueue.offer(order);
    }

    @Override
    public void placeOffer(final String name, final String instrument, final int quantity, final double price) {
        final MutableOrder order = new MutableOrder(name, instrument, Side.OFFER, quantity, DecimalUtil.fromDouble(price));
        eventQueue.offer(order);
    }

    @Override
    public void display(final String instrument) {
        final DisplayEvent displayEvent = new DisplayEvent(instrument);
        eventQueue.offer(displayEvent);
    }

    public void start(){
        executorService.execute(this::run);
    }


    public void stop(){
        System.out.println("Shutting down");
        executorService.shutdownNow();
    }

    private void run() {
        while(!Thread.currentThread().isInterrupted()){
            final Event event = (Event) eventQueue.poll();
            if(event instanceof MutableOrder){
                onOrder((MutableOrder)event);
            } else if (event instanceof DisplayEvent){
                onDisplay((DisplayEvent)event);
            } else if (event instanceof ExecutionReport){
                onExecutionReport((ExecutionReport)event);
            } else {
                //Poor man's idle strategy
                //Gentler on cpu
                LockSupport.parkNanos(1000);
            }
        }
    }

    private void onOrder(final MutableOrder order){
        final OrderBook orderBook = orderBooks.get(order.instrument());
        if(orderBook != null){
            orderBook.processOrder(order);
        }
    }

    private void onDisplay(final DisplayEvent displayEvent){
        final OrderBook orderBook = orderBooks.get(displayEvent.instrument());
        if(orderBook != null){
            onTopOfBookReport(orderBook.topOfBookRpt());
        }
    }

    private void onTopOfBookReport(final TopOfBookReport report){
        final String result = String.format("%d %s | %s %d",
                report.bestBidQuantity(),
                DecimalUtil.format(DecimalUtil.toDouble(report.bestBid())),
                DecimalUtil.format(DecimalUtil.toDouble(report.bestOffer())),
                report.bestOfferQuantity());
        consoleConsumer.accept(result);
    }

    private void onExecutionReport(final ExecutionReport execRpt){
        final String result;
        if(execRpt.ordStatus() == OrdStatus.FILLED){
            result = String.format("%s %s %d %s from %s @ %s",
                    execRpt.cpty(),
                    execRpt.cptySide().verb(),
                    execRpt.quantity(),
                    execRpt.instrument(),
                    execRpt.user(),
                    DecimalUtil.format(DecimalUtil.toDouble(execRpt.price())));
        } else {
            result = String.format("%s %s %d %s @ %s %s (%s)",
                    execRpt.user(),
                    execRpt.side(),
                    execRpt.quantity(),
                    execRpt.instrument(),
                    DecimalUtil.format(DecimalUtil.toDouble(execRpt.price())),
                    execRpt.ordStatus(),
                    execRpt.reason());
        }
        consoleConsumer.accept(result);
    }
}
