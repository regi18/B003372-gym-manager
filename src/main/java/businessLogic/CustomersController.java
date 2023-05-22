package businessLogic;

import dao.CustomerDAO;
import domainModel.Customer;
import domainModel.membership.Membership;
import domainModel.membership.EmptyMembership;
import domainModel.membership.WeekdaysMembershipDecorator;
import domainModel.membership.WeekendMembershipDecorator;

import java.time.LocalDate;


public class CustomersController extends PeopleController<Customer> {

    public CustomersController(CustomerDAO customerDAO) {
        super(customerDAO);
    }

    /**
     * @param fiscalCode           The fiscal of the new customer
     * @param name                 The name of the new customer
     * @param surname              The surname of the new customer
     * @param membershipDecorators The decorators to apply to the membership (e.g. "weekend", "weekdays", ...)
     * @param membershipEndDate    The membership end date
     *
     * @return The fiscal code of the newly created customer
     *
     * @throws Exception bubbles up exceptions of PeopleController::addPerson()
     */
    public String addPerson(String fiscalCode, String name, String surname, String[] membershipDecorators, LocalDate membershipEndDate) throws Exception {
        Membership m = new EmptyMembership(LocalDate.now(), membershipEndDate);

        for (String s : membershipDecorators) {
            if (s.equals("weekend")) m = new WeekendMembershipDecorator(m);
            else if (s.equals("weekdays")) m = new WeekdaysMembershipDecorator(m);
        }

        Customer c = new Customer(fiscalCode, name, surname, m);
        return super.addPerson(c);
    }
}
