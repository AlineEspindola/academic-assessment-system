package domain.course;

import domain.assessment.Assessment;
import domain.attendance.Attendance;
import domain.primitive.ID;
import domain.primitive.Score;
import domain.semester.Semester;
import domain.student.Student;
import domain.teacher.Teacher;

import java.util.Map;

public interface Course {
    ID id();

    String name();

    Teacher teacher();

    Student student(ID studentId);

    Map<ID, Student> students();

    Attendance attendanceFor(ID studentId);

    Semester semester();

    Course registerTeacher(Teacher teacher);

    Course registerStudents(Map<ID, Student> students);

    Course start();

    Course finish();

    Course addAssessment(int bimonthlyOrder, ID studentId, Assessment assessment);

    Course finishFirstBimonthly();

    Course finishSecondBimonthly();

    Course updateStudentState(ID studentId, Student newState);

    Course updateAttendance(ID studentId, Attendance attendance);

    String status();
}
