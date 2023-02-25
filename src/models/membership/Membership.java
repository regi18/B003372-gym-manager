package models.membership;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Gym membership interface
 * <p>
 * Defines the methods that a membership must implement in order to check if a customer can enter the gym
 */
public interface Membership {

    /**
     * Get the price of the membership
     *
     * @return float - price of the membership
     */
    public float getPrice();

    /**
     * Get the date from which the membership start
     *
     * @return LocalDate - date from which the membership start
     */
    public LocalDate getValidFrom();

    /**
     * Get the date in which the membership expires
     *
     * @return LocalDate - date in which the membership ends
     */
    public LocalDate getValidUntil();

    /**
     * Check if the membership is valid in the current date
     *
     * @return boolean - true if the membership is valid, false otherwise
     */
    public boolean isExpired();

    /**
     * Check if the membership is valid in the current interval of time
     *
     * @param start Start of the time interval
     * @param end   End of the time interval
     * @return boolean - true if the membership is valid for the current interval, false otherwise
     */
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end);
}
