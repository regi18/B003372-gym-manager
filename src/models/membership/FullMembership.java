package models.membership;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Concrete class representing a membership that doesn't have access restrictions.
 * Customers can enter the gym in any day and at any time
 */
public class FullMembership implements Membership {
    /** Price of the membership */
    private final float price;

    /** Date from which the membership starts */
    private final LocalDate validFrom;

    /** Date in which the membership ends */
    private final LocalDate validUntil;

    /**
     * Constructor of a full membership
     *
     * @param price      Price of the membership
     * @param validFrom  LocalDate from which the membership starts
     * @param validUntil LocalDate in which the membership ends
     *
     * @throws IllegalArgumentException if the price is negative
     */
    public FullMembership(float price, LocalDate validFrom, LocalDate validUntil) {
        if (price < 0) throw new IllegalArgumentException("Price must be greater than 0 (given: " + price + ")");

        this.price = price;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public LocalDate getValidFrom() {
        return validFrom;
    }

    @Override
    public LocalDate getValidUntil() {
        return validUntil;
    }

    @Override
    public boolean isExpired() {
        return validUntil.isBefore(LocalDateTime.now().toLocalDate());
    }

    @Override
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end) {
        return validFrom.isBefore(start.toLocalDate()) && validFrom.isBefore(end.toLocalDate()) && validUntil.isAfter(end.toLocalDate()) && validUntil.isAfter(start.toLocalDate());
    }
}
