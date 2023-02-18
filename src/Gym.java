import managers.CoursesManager;
import managers.PeopleManager;
import models.Customer;
import models.Trainer;

public class Gym {
    private String name;
    private String address;
    private final CoursesManager coursesManager;
    private final PeopleManager<Customer> customersManager;
    private final PeopleManager<Trainer> trainersManager;

    public Gym(String name, String address) {
        this.name = name;
        this.address = address;
        this.coursesManager = new CoursesManager();
        this.customersManager = new PeopleManager<Customer>();
        this.trainersManager = new PeopleManager<Trainer>();
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
