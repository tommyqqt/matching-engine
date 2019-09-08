package au.com.tommyq.market.engine;

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
    public List<MutableOrder> restingOrders() {
        return Collections.unmodifiableList(this.restingOrders);
    }

    @Override
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

    @Override
    public boolean clear(){
        if(quantity == 0){
            restingOrdersSize = 0;
            return true;
        }
        return false;
    }
}
