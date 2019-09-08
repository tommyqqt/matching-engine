package au.com.tommyq.market.engine;

public class DisplayEvent implements Event {
    private final String instrument;

    public DisplayEvent(final String instrument) {
        this.instrument = instrument;
    }

    public String instrument() {
        return this.instrument;
    }
}
