package domain.course;

import domain.primitive.ID;
import domain.semester.Semester;
import domain.student.Student;
import domain.teacher.Teacher;

import java.util.Map;

public interface Course {
    ID id();

    void register_teacher(Teacher teacher);

    void register_students(Map<ID, Student> students);

    Teacher teacher();

    Student student(ID id);

    Course start();

    Course finish();

    Semester start_semester(Semester semester);

    Semester finish_semester(Semester semester);

}
