package au.com.tommyq.market.engine;

public interface OrderBook {
    void processOrder(MutableOrder order);
    String instrument();
    TopOfBookReport topOfBookRpt();
}
