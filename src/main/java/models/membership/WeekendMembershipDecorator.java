package models.membership;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Decorator that limits the membership validity only in weekends
 */
public class WeekendMembershipDecorator extends MembershipDecorator {
    public WeekendMembershipDecorator(Membership membership) {
        super(membership);
    }

    @Override
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end) {
        return this.membership.isValidForInterval(start, end) && isOnWeekend(start) && isOnWeekend(end);
    }

    private boolean isOnWeekend(LocalDateTime date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    @Override
    public String toString() {
        return "WeekendMembershipDecorator{" + membership.toString() + "}";
    }
}
