package domainModel;
import domainModel.membership.Membership;


public class Customer extends Person {
    private Membership membership;

    public Customer(String fiscalCode, String name, String surname, Membership membership) {
        super(fiscalCode, name, surname);
        this.membership = membership;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
}
