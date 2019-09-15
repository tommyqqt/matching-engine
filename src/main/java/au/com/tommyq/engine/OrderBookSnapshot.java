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

public class OrderBookSnapshot implements Event {
    private final String instrument;
    private final PriceLevel[] depthSnapshot;
    private final int bestBidIndex;
    private final int bestOfferIndex;

    public OrderBookSnapshot(final String instrument, final PriceLevel[] depthSnapshot, final int bestBidIndex, final int bestOfferIndex) {
        this.instrument = instrument;
        this.depthSnapshot = depthSnapshot;
        this.bestBidIndex = bestBidIndex;
        this.bestOfferIndex = bestOfferIndex;
    }

    public String getInstrument() {
        return instrument;
    }

    public PriceLevel[] getDepthSnapshot() {
        return depthSnapshot;
    }

    public int bestBidIndex() {
        return this.bestBidIndex;
    }

    public int bestOfferIndex() {
        return this.bestOfferIndex;
    }
}
