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
    public PriceSnapshot {
        if (bid < 0 || ask < 0) {
            throw new IllegalArgumentException(
                    "Prices cannot be negative");
        }
        if (bid > ask) {
            throw new IllegalArgumentException(
                    "Bid cannot exceed ask");
        }
        if (timestamp <= 0 || sequence <= 0) {
            throw new IllegalArgumentException(
                    "Invalid timestamp or sequence");
        }
    }
}
