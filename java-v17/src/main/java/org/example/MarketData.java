package org.example;

/**
 * Java automatically generates:
 * - A canonical constructor
 * - Private final component fields
 * - Public accessor methods
 * - equals()
 * - hashCode()
 * - toString()
 * @param symbol
 * @param price
 * @param timestamp
 */

//Records are shallowly immutable. Records are useful for representing immutable trading messages,
// market data events, and API DTOs.
//However, records are not automatically the best choice for allocation-sensitive hot paths.
public record MarketData (
    String symbol,
    double price,
    long timestamp
){}
