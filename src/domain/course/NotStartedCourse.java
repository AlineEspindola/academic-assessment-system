package domain.course;

import domain.assessment.Assessment;
import domain.attendance.Attendance;
import domain.attendance.StudentAttendance;
import domain.primitive.ID;
import domain.primitive.Score;
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
    private final Semester semester;
    private Teacher teacher;
    private Map<ID, Student> students;

    public NotStartedCourse(ID id, String name, Semester semester) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.students = new HashMap<>();
    }

    @Override
    public ID id() { return id; }

    @Override
    public String name() { return name; }

    @Override
    public Teacher teacher() { return teacher; }

    @Override
    public Student student(ID studentId) {
        Student studentFound = students.get(studentId);
        if (studentFound == null) throw new IllegalArgumentException("Student " + studentId.value() + " not found in this course.");
        return studentFound;
    }

    @Override
    public Map<ID, Student> students() { return Map.copyOf(students); }

    @Override
    public Attendance attendanceFor(ID studentId) {
        throw new IllegalStateException("Course has not started — no attendance records available.");
    }

    @Override
    public Semester semester() { return semester; }

    @Override
    public Course registerTeacher(Teacher teacher) {
        this.teacher = teacher;
        return this;
    }

    @Override
    public Course registerStudents(Map<ID, Student> newStudents) {
        this.students.putAll(newStudents);
        return this;
    }

    @Override
    public Course start() {
        if (teacher == null) throw new IllegalStateException("Cannot start course without a registered teacher.");
        if (students.isEmpty()) throw new IllegalStateException("Cannot start course without registered students.");

        Map<ID, Student> studying = new HashMap<>();
        for (Map.Entry<ID, Student> entry : students.entrySet()) {
            studying.put(entry.getKey(), new StudyingStudent(entry.getValue()));
        }

        Map<ID, Attendance> attendances = new HashMap<>();
        for (ID sid : studying.keySet()) {
            attendances.put(sid, new StudentAttendance(sid));
        }

        Teacher teaching = new TeachingTeacher(teacher);
        Semester started = semester.start();

        return new InProgressCourse(id, name, teaching, studying, attendances, started);
    }

    @Override
    public Course finish() {
        throw new IllegalStateException("Cannot finish a course that has not started.");
    }

    @Override
    public Course addAssessment(int bimonthlyOrder, ID studentId, Assessment assessment) {
        throw new IllegalStateException("Cannot add assessments — course has not started.");
    }

    @Override
    public Course finishFirstBimonthly() {
        throw new IllegalStateException("Cannot finish bimonthly — course has not started.");
    }

    @Override
    public Course finishSecondBimonthly() {
        throw new IllegalStateException("Cannot finish bimonthly — course has not started.");
    }

    @Override
    public Course updateStudentState(ID studentId, Student newState) {
        throw new IllegalStateException("Cannot update student state — course has not started.");
    }

    @Override
    public Course updateAttendance(ID studentId, Attendance attendance) {
        throw new IllegalStateException("Cannot update attendance — course has not started.");
    }

    @Override
    public String status() { return "NOT_STARTED"; }
}
