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

public class DefaultExecutionReport implements ExecutionReport {
    private final String instrument;
    private final String user;
    private final String cpty;
    private final Side side;
    private final Side cptySide;
    private final long price;
    private final int quantity;
    private final OrdStatus ordStatus;
    private final String reason;

    public DefaultExecutionReport(final String instrument,
                                  final String user,
                                  final Side side,
                                  final String cpty,
                                  final Side cptySide,
                                  final long price,
                                  final int quantity,
                                  final OrdStatus ordStatus,
                                  final String reason) {
        this.instrument = instrument;
        this.user = user;
        this.side = side;
        this.cpty = cpty;
        this.cptySide = cptySide;
        this.price = price;
        this.quantity = quantity;
        this.ordStatus = ordStatus;
        this.reason = reason;
    }

    @Override
    public String instrument() {
        return this.instrument;
    }

    @Override
    public String user() {
        return this.user;
    }

    @Override
    public String cpty() {
        return this.cpty;
    }

    @Override
    public Side side() {
        return this.side;
    }

    @Override
    public Side cptySide() {
        return this.cptySide;
    }

    @Override
    public long price() {
        return this.price;
    }

    @Override
    public int quantity() {
        return this.quantity;
    }

    @Override
    public OrdStatus ordStatus() {
        return ordStatus;
    }

    @Override
    public String reason() {
        return this.reason;
    }
}
