package org.example.marketdata;


//Immutable snapshots allow multiple reader threads to safely access a
// consistent collection of fields once the snapshot has been safely published.

//The fields are final, so they cannot be modified after construction.
public record PriceSnapshot (
        long bid,
        long ask,
        long timestamp,
        long sequence
) {
    public PriceSnapshot(long bid, long ask, long timestamp, long sequence) {
        if (bid > ask) {
            throw new IllegalArgumentException("Bid must be less than or equal to ask");
        }
        if (timestamp <= 0) {
            throw new IllegalArgumentException("Timestamp must be greater than zero");
        }
        if (sequence <= 0) {
            throw new IllegalArgumentException("Sequence must be greater than zero");
        }
        if (bid >= 0 && ask >= 0) {
            this.bid = bid;
            this.ask = ask;
            this.timestamp = timestamp;
            this.sequence = sequence;
        } else {
            throw new IllegalArgumentException(
                    "Prices cannot be negative");
        }
    }
}
