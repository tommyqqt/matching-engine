package au.com.tommyq.market.engine;

public enum Side {
    BID("bought"), OFFER("sold"), NULL_VAL("");

    private String verb;

    Side(final String verb) {
        this.verb = verb;
    }

    public String verb() {
        return this.verb;
    }
}
