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

public class TopOfBookReport implements Event {
    private final long bestBid;
    private final long bestOffer;
    private final int bestBidQuantity;
    private final int bestOfferQuantity;

    public TopOfBookReport(final long bestBid, final long bestOffer, final int bestBidQuantity, final int bestOfferQuantity) {
        this.bestBid = bestBid;
        this.bestOffer = bestOffer;
        this.bestBidQuantity = bestBidQuantity;
        this.bestOfferQuantity = bestOfferQuantity;
    }

    public long bestBid() {
        return this.bestBid;
    }

    public long bestOffer() {
        return this.bestOffer;
    }

    public int bestBidQuantity() {
        return this.bestBidQuantity;
    }

    public int bestOfferQuantity() {
        return this.bestOfferQuantity;
    }

    @Override
    public String toString() {
        return "TopOfBookReport{" +
                "bestBid=" + bestBid +
                ", bestBidQuantity=" + bestBidQuantity +
                ", bestOffer=" + bestOffer +
                ", bestOfferQuantity=" + bestOfferQuantity +
                '}';
    }
}
