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

public class Reporting {
    public static String reportExecRpt(final ExecutionReport execRpt) {
        final String result;
        if (execRpt.ordStatus() == OrdStatus.FILLED) {
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
        return result;
    }

    public static String reportTopOfBook(final TopOfBookReport rpt){
        return String.format("%d %s | %s %d",
                rpt.bestBidQuantity(),
                DecimalUtil.format(DecimalUtil.toDouble(rpt.bestBid())),
                DecimalUtil.format(DecimalUtil.toDouble(rpt.bestOffer())),
                rpt.bestOfferQuantity());
    }

    public static String reportBookSnapshot(final OrderBookSnapshot snapshot){
        final int rptBestBidIndex = snapshot.bestBidIndex();
        final int rptBestOfferIndex = snapshot.bestOfferIndex();
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%1$-15s ArrayIndex  Level   Quantity  | Orders\n", ""));
        final PriceLevel[] depthSnapshot = snapshot.getDepthSnapshot();
        for (int i = depthSnapshot.length - 1; i >= 0; i--){
            if(i == rptBestBidIndex && depthSnapshot[i] != null){
                sb.append(String.format("%1$-15s %2$-12s", "Best BID   -->", i));
            } else if ((i == rptBestOfferIndex) && depthSnapshot[i] != null) {
                sb.append(String.format("%1$-15s %2$-12s", "Best OFFER -->", i));
            } else {
                sb.append(String.format("%1$-15s %2$-12s", "", i));
            }
            sb.append(depthSnapshot[i] != null ? formatPriceLevel(depthSnapshot[i]) : "");
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String formatPriceLevel(final PriceLevel level){
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%1$-7s %2$-9s |", DecimalUtil.format(DecimalUtil.toDouble(level.price())), level.quantity()));
        sb.append(" ");

        for (Order order : level.restingOrder()) {
            sb.append(String.format("%1$-6s", order.quantity()));
        }

        return sb.toString();
    }
}
