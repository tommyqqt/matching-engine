package au.com.tommyq.market;

public class InstrumentConfig {
    private final String name;
    private final double minPrice;
    private final double maxPrice;

    public InstrumentConfig(final String name, final double minPrice, final double maxPrice) {
        this.name = name;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String name() {
        return this.name;
    }

    public double minPrice() {
        return this.minPrice;
    }

    public double maxPrice() {
        return this.maxPrice;
    }

    @Override
    public String toString() {
        return "InstrumentConfig{" +
                "name='" + name + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                '}';
    }
}
