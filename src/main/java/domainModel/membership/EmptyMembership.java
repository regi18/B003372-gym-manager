package domainModel.membership;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Concrete class representing a membership that can never access the gym.
 */
public class EmptyMembership implements Membership {
    /** Price of the membership */
    private final float price;

    /** Date from which the membership starts */
    private final LocalDate validFrom;

    /** Date in which the membership ends */
    private final LocalDate validUntil;

    /**
     * Constructor of a full membership
     *
     * @param validFrom  LocalDate from which the membership starts
     * @param validUntil LocalDate in which the membership ends (can be the same date as validFrom, for a single day membership)
     *
     * @throws IllegalArgumentException if the price is negative or the date range is invalid
     */
    public EmptyMembership(LocalDate validFrom, LocalDate validUntil) {
        if (!validFrom.isBefore(validUntil) && !validFrom.equals(validUntil))
            throw new IllegalArgumentException("Date range invalid: validFrom must be a date before or equal to validUntil");

        this.price = 0;
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
    public String getUsesDescription() {
        return "";
    }

    @Override
    public int getUses() {
        return 0;
    }

    @Override
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end) {
        return false;
    }
}
