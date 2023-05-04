package controllers;

import dao.CustomerDAO;
import models.Customer;
import models.membership.Membership;
import models.membership.EmptyMembership;
import models.membership.WeekdaysMembershipDecorator;
import models.membership.WeekendMembershipDecorator;

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
     */
    public void addPerson(String fiscalCode, String name, String surname, String[] membershipDecorators, LocalDate membershipEndDate) {
        Membership m = new EmptyMembership(LocalDate.now(), membershipEndDate);

        for (String s : membershipDecorators) {
            if (s.equals("weekend")) m = new WeekendMembershipDecorator(m);
            else if (s.equals("weekdays")) m = new WeekdaysMembershipDecorator(m);
        }

        Customer c = new Customer(fiscalCode, name, surname, m);
        super.addPerson(c);
    }
}
