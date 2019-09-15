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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Consumer;

public class DefaultOrderBook implements OrderBook {
    private static final Logger logger = LoggerFactory.getLogger(DefaultOrderBook.class);
    private static final DefaultPriceLevel NULL_LEVEL = new DefaultPriceLevel(DecimalUtil.MANTISSA_NULL_VAL, 0);
    private final String instrument;
    private long minPrice;
    private long maxPrice;

    private int topBookBid;
    private int topBookOffer;

    private final DefaultPriceLevel[] depth;
    private final Consumer<? super Event> outputEventPublisher;

    public DefaultOrderBook(final String instrument,
                            final long minPrice,
                            final long maxPrice,
                            final Consumer<? super Event> outputEventPublisher) {

        this.outputEventPublisher = Objects.requireNonNull(outputEventPublisher);

        this.instrument = instrument;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;

        depth = new DefaultPriceLevel[(int)(maxPrice - minPrice) + 1];
        initDepth();

        this.topBookBid = -1;
        this.topBookOffer = depth.length;
    }

    private void initDepth(){
        long price = minPrice;
        for(int i = 0; i < depth.length; i++){
            depth[i] = new DefaultPriceLevel(price, 0);
            price++;
        }
    }

    @Override
    public void processOrder(final MutableOrder order){
        if(order.price() < minPrice || order.price() > maxPrice){
            logger.error("Order price={} is out of configured price range from={} to={}", minPrice, maxPrice);
            outputEventPublisher.accept(rejectExecRpt(order, "Order price is out of configured price range"));
            return;
        }

        final int levelIndex = (int)(order.price() - minPrice);
        final DefaultPriceLevel level = depth[levelIndex];

        switch(order.side()){
            case BID:
                crossBidOrder(order);
                if(order.quantity() > 0){
                    level.clear();
                    level.addOrder(order);
                    if(topBookBid < 0 || level.price() > depth[topBookBid].price()){
                        topBookBid = levelIndex;
                    }
                }
                return;
            case OFFER:
                crossOfferOrder(order);
                if(order.quantity() > 0){
                    level.clear();
                    level.addOrder(order);
                    if(topBookOffer >= depth.length || level.price() < depth[topBookOffer].price()){
                        topBookOffer = levelIndex;
                    }
                }
                return;
            default:
                logger.error("Invalid order side {}", order.side());
                outputEventPublisher.accept(rejectExecRpt(order, "Invalid order side"));
                return;
        }
    }

    private void crossBidOrder(final MutableOrder bidOrder){
        while(topBookOffer < depth.length){
            DefaultPriceLevel level = depth[topBookOffer];

            //cross resting orders at this level
            if(bidOrder.price() >= level.price()){
                for(MutableOrder restingOrder : level.restingMutableOrders()){
                    if(bidOrder.quantity() == 0){
                        return;
                    }
                    cross(restingOrder, bidOrder, level);
                }
                if(level.clear()){
                    topBookOffer++;
                } else {
                    return;
                }
            } else if (level.quantity() == 0){
                topBookOffer++;
            } else {
                return;
            }
        }
    }

    private void crossOfferOrder(final MutableOrder offerOrder){
        while(topBookBid >= 0){
            DefaultPriceLevel level = depth[topBookBid];

            //cross resting orders at this level
            if(offerOrder.price() <= level.price()){
                for(MutableOrder restingOrder : level.restingMutableOrders()){
                    if(offerOrder.quantity() == 0){
                        return;
                    }
                    cross(restingOrder, offerOrder, level);
                }
                if(level.clear()){
                    topBookBid--;
                } else {
                    return;
                }
            } else if (level.quantity() == 0) {
                topBookBid--;
            } else {
                return;
            }
        }
    }

    private void cross(final MutableOrder restingOrder, final MutableOrder order, final DefaultPriceLevel level){
        if(restingOrder.quantity() == 0){
            return;
        }
        final int depletedQuantity = restingOrder.quantity() >= order.quantity() ? order.quantity() : restingOrder.quantity();
        restingOrder.quantity(restingOrder.quantity() - depletedQuantity);
        order.quantity(order.quantity() - depletedQuantity);
        level.quantity(level.quantity() - depletedQuantity);
        final ExecutionReport execRpt = execRpt(restingOrder, order, depletedQuantity);
        outputEventPublisher.accept(execRpt);
    }

    private ExecutionReport execRpt(final MutableOrder restingOrder, final MutableOrder order, final int execQty){
        return new DefaultExecutionReport(order.instrument(),
                order.user(),
                order.side(),
                restingOrder.user(),
                restingOrder.side(),
                restingOrder.price(),
                execQty,
                OrdStatus.FILLED,
                "");
    }

    private ExecutionReport rejectExecRpt(final MutableOrder order, final String rejectReason){
        return new DefaultExecutionReport(order.instrument(),
                order.user(),
                order.side(),
                "",
                Side.NULL_VAL,
                order.price(),
                order.quantity(),
                OrdStatus.REJECTED,
                rejectReason);
    }

    @Override
    public String instrument() {
        return instrument;
    }

    @Override
    public TopOfBookReport topOfBookRpt(){
        final DefaultPriceLevel bestBidLevel = topBookBid > -1 ? depth[topBookBid] : NULL_LEVEL;
        final DefaultPriceLevel bestOfferLevel = topBookOffer < depth.length ?  depth[topBookOffer] : NULL_LEVEL;
        return new TopOfBookReport(bestBidLevel.price(), bestOfferLevel.price(), bestBidLevel.quantity(), bestOfferLevel.quantity());
    }

    @Override
    public OrderBookSnapshot snapshot(final int depthOfBook){
        final PriceLevel[] reportedDepth;
        final int rptTopBookBid;
        final int rptTopBookOffer;
        if(depthOfBook < 0){
            rptTopBookBid = topBookBid;
            rptTopBookOffer = topBookOffer;
            reportedDepth = new PriceLevel[depth.length];
            for(int i = 0; i < depth.length; i++){
                reportedDepth[i] = depth[i];
            }
        } else {
            reportedDepth = new PriceLevel[depthOfBook * 2];
            int bidBookIndex = topBookBid;
            int offerBookIndex = topBookOffer;
            int rptBookBidIndex = depthOfBook - 1;
            int rptBookOfferIndex = depthOfBook;
            rptTopBookBid = rptBookBidIndex;
            rptTopBookOffer = rptBookOfferIndex;

            while (bidBookIndex >= 0
                    && rptBookBidIndex >= 0
                    && offerBookIndex < depth.length
                    && rptBookOfferIndex < reportedDepth.length)
            {
                reportedDepth[rptBookBidIndex] = depth[bidBookIndex];
                reportedDepth[rptBookOfferIndex] = depth[offerBookIndex];
                bidBookIndex--;
                rptBookBidIndex--;
                offerBookIndex++;
                rptBookOfferIndex++;
            }
        }

        return new OrderBookSnapshot(instrument, reportedDepth, rptTopBookBid, rptTopBookOffer);
    }

}
