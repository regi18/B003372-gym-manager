package view;

import controllers.BookerService;
import controllers.PeopleController;
import models.Course;
import models.Customer;
import utils.observer.Observer;
import utils.observer.events.CourseDeletedEvent;
import utils.observer.events.Event;

public class CustomerView implements Observer {
    PeopleController<Customer> customerController;
    BookerService bookerService;

    public CustomerView() {
        this.customerController = new PeopleController<>();
        this.bookerService = new BookerService(null); // TODO coursesController deve essere "globale"
    }


    @Override
    public void update(Event event) {
        if (event instanceof CourseDeletedEvent) {
            Course c = ((CourseDeletedEvent) event).getRemovedCourse();
            // TODO
//            System.out.println("[Customer] [Notification for user '" + this.getFiscalCode() + "'] The course '" + c.getName() + "' has been canceled");
        }
    }
}
