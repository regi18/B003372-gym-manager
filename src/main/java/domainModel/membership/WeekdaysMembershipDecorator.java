package domainModel.membership;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Decorator that lets the membership be valid in weekdays
 */
public class WeekdaysMembershipDecorator extends MembershipDecorator {
    int uses;

    public WeekdaysMembershipDecorator(Membership membership) {
        super(membership);
    }

    public WeekdaysMembershipDecorator(Membership membership, int uses) {
        super(membership);
        this.uses = uses;
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
    public String getUsesDescription() {
        return super.getUsesDescription() + "Weekdays uses: " + uses + ", ";
    }

    @Override
    public HashMap<String, Integer> getUses() {
        this.membership.getUses().put("weekdays", uses);
        return this.membership.getUses();
    }

    private boolean isOnWeekdays(LocalDateTime date) {
        return date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY;
    }
}
