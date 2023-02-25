package models.membership;

import java.time.LocalDate;

/** Abstract membership decorator */
public abstract class MembershipDecorator implements Membership {

    /** Membership to decorate */
    protected final Membership membership;

    /**
     * Constructs a new membership
     *
     * @param membership Membership to decorate
     */
    public MembershipDecorator(Membership membership) {
        this.membership = membership;
    }

    @Override
    public float getPrice() {
        return this.membership.getPrice();
    }

    @Override
    public LocalDate getValidFrom() {
        return this.membership.getValidFrom();
    }

    @Override
    public LocalDate getValidUntil() {
        return this.membership.getValidUntil();
    }

    @Override
    public boolean isExpired() {
        return this.membership.isExpired();
    }
}
