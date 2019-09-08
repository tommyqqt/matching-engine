package au.com.tommyq.market.engine;

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

    @Override
    public void quantity(final int quantity) {
        this.quantity = quantity;
    }

    @Override
    public long price() {
        return this.price;
    }
}
