import models.Course;
import models.Customer;
import models.Trainer;
import models.membership.FullMembership;
import models.membership.Membership;
import models.membership.WeekendMembershipDecorator;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {

        Gym gymTest = new Gym("testGym", "via Roma 47, Firenze");
        Trainer tmp = new Trainer("A", "B", "C", 10);

        gymTest.getCoursesManager().addCourse(new Course("Spinning", 10, LocalDateTime.parse("2023-03-20T13:00:00"), LocalDateTime.parse("2023-03-20T14:00:00"), tmp));
        gymTest.getCoursesManager().addCourse(new Course("Calisthenics", 10, LocalDateTime.parse("2023-03-15T20:00:00"), LocalDateTime.parse("2023-03-15T21:30:00"), tmp));
        gymTest.getCoursesManager().addCourse(new Course("Yoga", 10, LocalDateTime.parse("2023-03-18T09:00:00"), LocalDateTime.parse("2023-03-18T10:00:00"), tmp));

        Membership fullMembership = new FullMembership(300, LocalDate.now(), LocalDate.now().plusYears(1));
        Membership weekendMembership = new WeekendMembershipDecorator(new FullMembership(300, LocalDate.now(), LocalDate.now().plusYears(1)));

        gymTest.getCustomersManager().addPerson(new Customer("RSSMRA000", "Mario", "Rossi", fullMembership));
        gymTest.getCustomersManager().addPerson(new Customer("VRDLGI111", "Luigi", "Verdi", weekendMembership));

        // USE CASE: "Prenota Corso"
        System.out.println("Booking course...");
        Customer c1 = gymTest.getCustomersManager().getPerson("VRDLGI111");
        Customer c2 = gymTest.getCustomersManager().getPerson("RSSMRA000");
        gymTest.getBookerService().bookCourse(c1, 2);
//        gymTest.getBookerService().bookCourse(c1, 0);
        gymTest.getBookerService().bookCourse(c2, 0);
        gymTest.getBookerService().bookCourse(c2, 2);

        gymTest.getCoursesManager().deleteCourse(2);
    }
}