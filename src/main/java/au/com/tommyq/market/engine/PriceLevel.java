package au.com.tommyq.market.engine;

import java.util.List;

public interface PriceLevel {
    long price();

    int quantity();

    void quantity(int quantity);

    List<MutableOrder> restingOrders();

    void addOrder(final MutableOrder order);

    boolean clear();
}
