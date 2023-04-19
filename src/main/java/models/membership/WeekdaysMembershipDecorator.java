package models.membership;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Decorator that lets the membership be valid in weekdays
 */
public class WeekdaysMembershipDecorator extends MembershipDecorator {
    int uses;

    public WeekdaysMembershipDecorator(Membership membership) {
        super(membership);
    }

    @Override
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end) {
        if (!this.isDateIntervalValid(start, end)) return false;
        boolean isCurrTrue = (isOnWeekdays(start) && isOnWeekdays(end));
        if (isCurrTrue) uses++;
        return super.isValidForInterval(start, end) || isCurrTrue;
    }

    @Override
    public float getPrice() {
        return super.getPrice() + 250;
    }

    @Override
    public String getUses() {
        return super.getUses() + "Weekdays uses: " + uses + ", ";
    }

    private boolean isOnWeekdays(LocalDateTime date) {
        return date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY;
    }
}
