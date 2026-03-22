package domain.course;

import domain.primitive.ID;
import domain.semester.NotStartedSemester;
import domain.semester.Semester;
import domain.student.Student;
import domain.student.StudyingStudent;
import domain.teacher.Teacher;
import domain.teacher.TeachingTeacher;

import java.util.HashMap;
import java.util.Map;

public class NotStartedCourse implements Course {
    private final ID id;
    private final String name;
    private Teacher teacher;
    private HashMap<ID, Student> students;
    private Semester semester;

    public NotStartedCourse(ID id, String name, Semester semester) {
        this.id = id;
        this.name = name;
        this.students = new HashMap<>();
        this.semester = semester;
    }

    public NotStartedCourse(ID id, String name, HashMap<ID, Student> students) {
        this.id = id;
        this.name = name;
        this.students = students;
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
        this.students.putAll(students);
    }

    @Override
    public Teacher teacher() {
        return teacher;
    }

    @Override
    public Student student(ID id) {
        Student student = students.get(id);

        if (student == null) {
            throw new IllegalArgumentException("Student with id " + id + " is not registered in this course.");
        }

        return student;
    }

    @Override
    public Course start() {
        students.replaceAll((id, student) -> new StudyingStudent(student));
        teacher = new TeachingTeacher(teacher);
        return new InProgressCourse(this);
    }

    @Override
    public Course finish() {
        return null;
    }

    @Override
    public Semester start_semester(Semester semester) {
        throw new IllegalStateException("Cannot start the semester without having started the course.");
    }

    @Override
    public Semester finish_semester(Semester semester) {
        throw new IllegalStateException("Cannot start the semester without having started the course.");
    }
}
