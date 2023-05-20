package domainModel.membership;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Gym membership interface.
 * Defines the methods that a membership must implement in order to check if a customer can enter the gym
 */
public interface Membership {

    /**
     * Get the price of the membership
     *
     * @return float - price of the membership
     */
    float getPrice();

    /**
     * Get the date from which the membership start
     *
     * @return LocalDate - date from which the membership start
     */
    LocalDate getValidFrom();

    /**
     * Get the date in which the membership expires
     *
     * @return LocalDate - date in which the membership ends
     */
    LocalDate getValidUntil();

    /**
     * Check if the membership is valid in the current date
     *
     * @return boolean - true if the membership is valid, false otherwise
     */
    boolean isExpired();

    /**
     * Check if the membership is valid in the current interval of time
     *
     * @param start Start of the time interval
     * @param end   End of the time interval
     *
     * @return boolean - true if the membership is valid for the current interval, false otherwise
     */
    boolean isValidForInterval(LocalDateTime start, LocalDateTime end);

    /**
     * Get how many times the various membership decorators have been useful to grant access
     *
     * @return The string with the details
     */
    String getUsesDescription();

    /**
     * Get how many times the membership has been useful to grant access
     *
     * @return The number of uses
     */
    int getUses();
}
