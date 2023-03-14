package models.membership;

import java.time.LocalDateTime;

/**
 * Decorator that limits the membership validity only after the hour 20:00
 */
public class EveningMembershipDecorator extends MembershipDecorator {

    public EveningMembershipDecorator(Membership membership) {
        super(membership);
    }

    @Override
    public boolean isValidForInterval(LocalDateTime start, LocalDateTime end) {
        return this.membership.isValidForInterval(start, end) && isOnEvening(start) && isOnEvening(end);
    }

    private boolean isOnEvening(LocalDateTime date) {
        return date.getHour() >= 20;
    }
}
