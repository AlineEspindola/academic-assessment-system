import domain.assessment.Assessment;
import domain.assessment.InProgressAssessment;
import domain.assessment.NotStartedAssessment;
import domain.course.Course;
import domain.course.NotStartedCourse;
import domain.primitive.*;
import domain.semester.NotStartedSemester;
import domain.semester.Semester;
import domain.student.NotStartedStudent;
import domain.student.Student;
import domain.teacher.IdleTeacher;
import domain.teacher.Teacher;

import java.time.LocalDate;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Student student_ana = new NotStartedStudent(new DefaultID("1"), "Ana Souze Silva", 202601);
        Student student_felipe = new NotStartedStudent(new DefaultID("2"), "Felipe da Cunha", 202602);

        Teacher teacher_gabriel = new IdleTeacher(new DefaultID("1"), "Gabriel Rodrigues");

        Semester semester = new NotStartedSemester(new DefaultID("1"), LocalDate.of(2026, 3, 22), LocalDate.of(2026, 6, 22));

        Course course_mathematics = new NotStartedCourse(new DefaultID("1"), "Matemática", semester);

        course_mathematics.register_teacher(teacher_gabriel);

        HashMap<ID, Student> students = new HashMap<>();

        students.put(student_ana.id(), student_ana);
        students.put(student_felipe.id(), student_felipe);

        course_mathematics.register_students(students);

        course_mathematics.start();

        System.out.println(course_mathematics.teacher().status());
        System.out.println(course_mathematics.student(new DefaultID("1")).status());
    }
}