package au.com.tommyq.market.engine;

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
