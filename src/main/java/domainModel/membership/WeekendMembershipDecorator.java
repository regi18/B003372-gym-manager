package domainModel.membership;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Decorator that lets the membership be valid in weekdays
 */
public class WeekendMembershipDecorator extends MembershipDecorator {
    int uses;

    public WeekendMembershipDecorator(Membership membership) {
        super(membership);
    }

    public WeekendMembershipDecorator(Membership membership, int uses) {
        super(membership);
        this.uses = uses;
    }

    @Override
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end) {
        if (!this.isDateIntervalValid(start, end)) return false;
        boolean isCurrTrue = (isOnWeekend(start) && isOnWeekend(end));
        if (isCurrTrue) uses++;
        return super.isValidForInterval(start, end) || isCurrTrue;
    }

    @Override
    public float getPrice() {
        return super.getPrice() + 50;
    }

    @Override
    public String getUsesDescription() {
        return super.getUsesDescription() + "Weekend uses: " + uses + ", ";
    }

    @Override
    public HashMap<String, Integer> getUses() {
        this.membership.getUses().put("weekend", uses);
        return this.membership.getUses();
    }

    private boolean isOnWeekend(LocalDateTime date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    @Override
    public int getLocalUses() {
        return uses;
    }
}
