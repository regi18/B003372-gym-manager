package view;

import controllers.BookingsController;
import controllers.PeopleController;
import models.Course;
import models.Customer;

import java.util.Scanner;

public class CustomerView {
    private final PeopleController<Customer> customerController;
    private final BookingsController bookingsController;
    private Customer currentCustomer;

    public CustomerView(PeopleController<Customer> customerController, BookingsController bookingsController) {
        this.customerController = customerController;
        this.bookingsController = bookingsController;
    }

    public void login(String fiscalCode) {
        this.currentCustomer = customerController.getPerson(fiscalCode);

        System.out.println("Welcome, ");
        showCustomerDetails();
    }

    public void showCustomerDetails() {
        System.out.println("Customer: " + currentCustomer.getName() + " " + currentCustomer.getSurname());
        System.out.println("Membership: " + currentCustomer.getMembership().toString());
        System.out.print("Booked courses: [");
        for (Course course : bookingsController.getBookingsForCustomer(currentCustomer.getFiscalCode())) {
            System.out.print(course.getName());
            System.out.print(", ");
        }
        System.out.println("]");
    }

    public void prompt() {
        System.out.println("What do you want to do?");
        System.out.println("1. Book a course");
        System.out.println("2. Cancel a booking");
        System.out.println("3. Change user");
        System.out.print("> ");

        Scanner reader = new Scanner(System.in);
        int res = reader.nextInt();
        reader.nextLine();
        System.out.println();

        switch (res) {
            case 1:
                System.out.print("Which course do you want to book? ");
                break;
            case 2:
                System.out.print("Which course do you want to cancel? ");
                break;
            case 3:
                System.out.print("Please write the code to change the user: ");
                this.login(reader.nextLine());
                break;
            default:
                System.out.println("Invalid response");
        }
        reader.close();
    }
}
