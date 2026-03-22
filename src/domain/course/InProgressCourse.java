package domain.course;

import domain.primitive.ID;
import domain.semester.Semester;
import domain.student.Student;
import domain.teacher.Teacher;

import java.util.Map;

public class InProgressCourse implements Course {
    private final Course course;

    public InProgressCourse(Course course) {
        this.course = course;
    }

    @Override
    public ID id() {
        return course.id();
    }

    @Override
    public void register_teacher(Teacher teacher) {
        course.register_teacher(teacher);
    }

    @Override
    public void register_students(Map<ID, Student> students) {
        course.register_students(students);
    }

    @Override
    public Teacher teacher() {
        return course.teacher();
    }

    @Override
    public Student student(ID id) {
        return course.student(id);
    }

    @Override
    public Course start() {
        throw new IllegalStateException(
                "Cannot start a course already in progress."
        );
    }

    @Override
    public Course finish() {
        return null;
    }

    @Override
    public Semester start_semester(Semester semester) {
        return null;
    }

    @Override
    public Semester finish_semester(Semester semester) {
        return null;
    }
}
