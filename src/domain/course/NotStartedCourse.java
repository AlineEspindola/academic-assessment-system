package domain.course;

import domain.primitive.ID;
import domain.semester.Semester;
import domain.student.Student;
import domain.teacher.Teacher;

import java.util.HashMap;
import java.util.Map;

public class NotStartedCourse implements Course {
    private final ID id;
    private final String name;
    private Teacher teacher;
    private HashMap<ID, Student> students;

    public NotStartedCourse(ID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public ID id() {
        return id;
    }

    @Override
    public void register_teacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public void register_students(Map<ID, Student> students) {

    }

    @Override
    public Course start() {
        return null;
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
