package managers;

import models.Course;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;


class CoursesManagerTest {
    private CoursesManager m;
    private Course testCourse;

    @Test
    @BeforeEach
    public void when_AddingNewCourse_Expect_Success() {
        m = new CoursesManager();
        testCourse = new Course("test", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        m.addCourse(testCourse);
    }

    @Test
    public void when_AddingAlreadyExistingCourse_Expect_IllegalArgumentException() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> m.addCourse(testCourse),
            "Expected addCourse() to throw, but it didn't"
        );
    }

    @Test
    public void when_gettingExistingCourse_Expect_toReturnThatCourse() {
        Course c1 = new Course("test2", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        m.addCourse(c1);
        Assertions.assertEquals(testCourse, m.getCourse(testCourse.getId()));
    }

    @Test
    public void when_gettingNonExistingCourse_Expect_toReturnNull() {
        Assertions.assertNull(m.getCourse(testCourse.getId() + 1));
    }

    @Test
    public void when_deletingExistingCourse_Expect_toReturnTrue() {
        Assertions.assertTrue(m.deleteCourse(testCourse.getId()));
    }

    @Test
    public void when_deletingNonExistingCourse_Expect_toReturnFalse() {
        Assertions.assertFalse(m.deleteCourse(testCourse.getId() + 1));
    }
}