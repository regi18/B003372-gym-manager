package domainModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDateTime;


class CourseTest {
    @Test
    public void When_CreatingNewCourse_With_InvalidDates_Expect_IllegalArgumentException() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new Course(0, "A", 10, LocalDateTime.now(), LocalDateTime.now().minusMinutes(1), Mockito.mock(Trainer.class)),
            "Expected constructor to throw, but it didn't"
        );

        LocalDateTime d = LocalDateTime.now();
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new Course(0, "A", 10, d, d, Mockito.mock(Trainer.class)),
            "Expected constructor to throw, but it didn't"
        );
    }
}