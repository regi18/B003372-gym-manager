package controllers;

import models.Course;
import models.Trainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;


class CoursesControllerTest {
    private CoursesController m;
    private Course testCourse;

    @Test
    @BeforeEach
    public void when_AddingNewCourse_Expect_Success() {
        m = new CoursesController();
        testCourse = new Course("test", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Mockito.mock(Trainer.class));
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
    public void when_GettingExistingCourse_Expect_ToReturnThatCourse() {
        Course c1 = new Course("test2", 10, LocalDateTime.now(), LocalDateTime.now().plusHours(1), Mockito.mock(Trainer.class));
        m.addCourse(c1);
        Assertions.assertEquals(testCourse, m.getCourse(testCourse.getId()));
    }

    @Test
    public void when_GettingNonExistingCourse_Expect_ToReturnNull() {
        Assertions.assertNull(m.getCourse(testCourse.getId() + 1));
    }

    @Test
    public void when_DeletingExistingCourse_Expect_ToReturnTrue() {
        Assertions.assertTrue(m.removeCourse(testCourse.getId()));
    }

    @Test
    public void when_DeletingNonExistingCourse_Expect_ToReturnFalse() {
        Assertions.assertFalse(m.removeCourse(testCourse.getId() + 1));
    }
}