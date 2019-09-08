package au.com.tommyq.market.engine;

public class TopOfBookReport implements Event {
    private final long bestBid;
    private final long bestOffer;
    private final int bestBidQuantity;
    private final int bestOfferQuantity;

    public TopOfBookReport(final long bestBid, final long bestOffer, final int bestBidQuantity, final int bestOfferQuantity) {
        this.bestBid = bestBid;
        this.bestOffer = bestOffer;
        this.bestBidQuantity = bestBidQuantity;
        this.bestOfferQuantity = bestOfferQuantity;
    }

    public long bestBid() {
        return this.bestBid;
    }

    public long bestOffer() {
        return this.bestOffer;
    }

    public int bestBidQuantity() {
        return this.bestBidQuantity;
    }

    public int bestOfferQuantity() {
        return this.bestOfferQuantity;
    }

    @Override
    public String toString() {
        return "TopOfBookReport{" +
                "bestBid=" + bestBid +
                ", bestBidQuantity=" + bestBidQuantity +
                ", bestOffer=" + bestOffer +
                ", bestOfferQuantity=" + bestOfferQuantity +
                '}';
    }
}
