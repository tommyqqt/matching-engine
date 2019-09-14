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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultPriceLevel implements PriceLevel {
    private long price;
    private int quantity;
    private int restingOrdersSize = 0;

    private final List<MutableOrder> restingOrders;

    public DefaultPriceLevel(final long price, final int quantity, final int restingOrdCapacity) {
        this.price = price;
        this.quantity = quantity;
        this.restingOrders = new ArrayList<>(restingOrdCapacity);
    }

    /**
     * Assume there are typically at most 20 resting orders at each price level
     */
    public DefaultPriceLevel(final long price, final int quantity) {
        this(price, quantity, 20);
    }

    @Override
    public long price() {
        return this.price;
    }

    public void price(final long price) {
        this.price = price;
    }

    @Override
    public int quantity() {
        return this.quantity;
    }

    @Override
    public void quantity(final int quantity) {
        this.quantity = quantity;
    }

    @Override
    public List<Order> restingOrder() {
        final List<Order> results = new ArrayList<>(restingOrdersSize);
        for(int i = 0; i < restingOrdersSize; i++){
            results.add(restingOrders.get(i));
        }
        return results;
    }

    public List<MutableOrder> restingMutableOrders() {
        return restingOrders;
    }

    public void addOrder(final MutableOrder order){
        if(restingOrdersSize < restingOrders.size()){
            final int index = restingOrdersSize == 0 ? 0 : restingOrdersSize - 1;
            restingOrders.set(index, order);
        } else {
            restingOrders.add(order);
        }
        this.quantity += order.quantity();
        restingOrdersSize++;
    }

    public boolean clear(){
        if(quantity == 0){
            restingOrdersSize = 0;
            return true;
        }
        return false;
    }
}
