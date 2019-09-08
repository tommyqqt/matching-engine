package au.com.tommyq.market.engine;

public interface ExecutionReport extends Event {
    String instrument();

    String user();

    String cpty();

    Side side();

    Side cptySide();

    long price();

    int quantity();

    OrdStatus ordStatus();

    String reason();

    @Override
    String toString();
}
