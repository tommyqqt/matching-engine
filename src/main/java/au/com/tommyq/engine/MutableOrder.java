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

public class MutableOrder implements Order {
    private String user;
    private String instrument;
    private Side side;
    private int quantity;
    private long price;

    public MutableOrder(final String user,
                        final String instrument,
                        final Side side,
                        final int quantity,
                        final long price) {
        this.user = user;
        this.instrument = instrument;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String user() {
        return this.user;
    }

    @Override
    public String instrument() {
        return this.instrument;
    }

    @Override
    public Side side() {
        return this.side;
    }

    @Override
    public int quantity() {
        return this.quantity;
    }

    public void quantity(final int quantity) {
        this.quantity = quantity;
    }

    @Override
    public long price() {
        return this.price;
    }
}
