import managers.CoursesManager;
import managers.PeopleManager;
import models.Customer;
import models.Trainer;
import services.BookerService;

public class Gym {
    private String name;
    private String address;
    private final CoursesManager coursesManager;
    private final PeopleManager<Customer> customersManager;
    private final PeopleManager<Trainer> trainersManager;
    private final BookerService bookerService;


    public Gym(String name, String address) {
        this.name = name;
        this.address = address;
        this.coursesManager = new CoursesManager();
        this.customersManager = new PeopleManager<>();
        this.trainersManager = new PeopleManager<>();
        this.bookerService = new BookerService(coursesManager);
    }


    // ----- GETTERS & SETTERS -----

    public CoursesManager getCoursesManager() {
        return coursesManager;
    }

    public PeopleManager<Customer> getCustomersManager() {
        return customersManager;
    }

    public PeopleManager<Trainer> getTrainersManager() {
        return trainersManager;
    }

    public BookerService getBookerService() {
        return bookerService;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
