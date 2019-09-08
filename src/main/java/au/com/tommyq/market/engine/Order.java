package au.com.tommyq.market.engine;

public interface Order extends Event {
    String user();

    String instrument();

    Side side();

    int quantity();

    void quantity(int quantity);

    long price();
}
