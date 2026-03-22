package domain.course;

import domain.assessment.Assessment;
import domain.attendance.Attendance;
import domain.primitive.ID;
import domain.primitive.Score;
import domain.semester.Semester;
import domain.student.Student;
import domain.teacher.Teacher;

import java.util.Map;

public class FinishedCourse implements Course {
    private final ID id;
    private final String name;
    private final Teacher teacher;
    private final Map<ID, Student> students;
    private final Map<ID, Attendance> attendances;
    private final Semester semester;

    public FinishedCourse(
            ID id,
            String name,
            Teacher teacher,
            Map<ID, Student> students,
            Map<ID, Attendance> attendances,
            Semester semester
    ) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.students = students;
        this.attendances = attendances;
        this.semester = semester;
    }

    @Override
    public ID id() { return id; }

    @Override
    public String name() { return name; }

    @Override
    public Teacher teacher() { return teacher; }

    @Override
    public Student student(ID studentId) {
        Student s = students.get(studentId);
        if (s == null) throw new IllegalArgumentException("Student " + studentId.value() + " not found.");
        return s;
    }

    @Override
    public Map<ID, Student> students() { return Map.copyOf(students); }

    @Override
    public Attendance attendanceFor(ID studentId) {
        Attendance a = attendances.get(studentId);
        if (a == null) throw new IllegalArgumentException("No attendance record for student " + studentId.value());
        return a;
    }

    @Override
    public Semester semester() { return semester; }

    @Override
    public Course registerTeacher(Teacher teacher) {
        throw new IllegalStateException("Course is already finished.");
    }

    @Override
    public Course registerStudents(Map<ID, Student> newStudents) {
        throw new IllegalStateException("Course is already finished.");
    }

    @Override
    public Course start() {
        throw new IllegalStateException("Course is already finished.");
    }

    @Override
    public Course finish() {
        throw new IllegalStateException("Course is already finished.");
    }

    @Override
    public Course addAssessment(int bimonthlyOrder, ID studentId, Assessment assessment) {
        throw new IllegalStateException("Course is already finished — cannot add assessments.");
    }

    @Override
    public Course finishFirstBimonthly() {
        throw new IllegalStateException("Course is already finished.");
    }

    @Override
    public Course finishSecondBimonthly() {
        throw new IllegalStateException("Course is already finished.");
    }

    @Override
    public Course updateStudentState(ID studentId, Student newState) {
        throw new IllegalStateException("Course is already finished.");
    }

    @Override
    public Course updateAttendance(ID studentId, Attendance attendance) {
        throw new IllegalStateException("Course is already finished.");
    }

    @Override
    public String status() { return "FINISHED"; }
}
