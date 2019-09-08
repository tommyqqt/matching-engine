package au.com.tommyq.market;

public interface MatchingEngine {
    void placeBid(final String name,
                  final String instrument,
                  final int quantity,
                  final double price);

    void placeOffer(final String name,
                    final String instrument,
                    final int quantity,
                    final double price);

    void display(final String instrument);
}
