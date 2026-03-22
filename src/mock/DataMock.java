package mock;

import domain.attendance.StudentAttendance;
import domain.bimonthly.NotStartedBimonthly;
import domain.course.Course;
import domain.course.NotStartedCourse;
import domain.primitive.DefaultID;
import domain.semester.NotStartedSemester;
import domain.semester.Semester;
import domain.student.NotStartedStudent;
import domain.student.Student;
import domain.teacher.IdleTeacher;
import domain.teacher.Teacher;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataMock {

    // ── IDs fixos para referência fácil na View ──────────────────────────────

    public static final DefaultID TEACHER_ID      = new DefaultID("T-001");
    public static final DefaultID STUDENT_ANA_ID  = new DefaultID("S-001");
    public static final DefaultID STUDENT_JOAO_ID = new DefaultID("S-002");
    public static final DefaultID SEMESTER_ID     = new DefaultID("SEM-2026-1");
    public static final DefaultID BIMESTRAL_1_ID  = new DefaultID("BIM-1");
    public static final DefaultID BIMESTRAL_2_ID  = new DefaultID("BIM-2");
    public static final DefaultID COURSE_ID       = new DefaultID("CRS-001");

    public static Course buildCourse() {
        // ── Professor ────────────────────────────────────────────────────────
        Teacher teacher = new IdleTeacher(TEACHER_ID, "Prof. Marcos Henrique");

        // ── Alunos ───────────────────────────────────────────────────────────
        Student ana  = new NotStartedStudent(STUDENT_ANA_ID,  "Ana Souza Silva",  202601);
        Student joao = new NotStartedStudent(STUDENT_JOAO_ID, "João da Cunha",    202602);

        Map<DefaultID, Student> students = new LinkedHashMap<>();
        students.put(STUDENT_ANA_ID,  ana);
        students.put(STUDENT_JOAO_ID, joao);

        // ── Bimestres ────────────────────────────────────────────────────────
        NotStartedBimonthly firstBimonthly = new NotStartedBimonthly(
                BIMESTRAL_1_ID, 1,
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 4, 30)
        );

        NotStartedBimonthly secondBimonthly = new NotStartedBimonthly(
                BIMESTRAL_2_ID, 2,
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 6, 30)
        );

        // ── Semestre ─────────────────────────────────────────────────────────
        Semester semester = new NotStartedSemester(
                SEMESTER_ID,
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 6, 30),
                firstBimonthly,
                secondBimonthly
        );

        // ── Curso ────────────────────────────────────────────────────────────
        Course course = new NotStartedCourse(COURSE_ID, "Matemática", semester);
        course = course.registerTeacher(teacher);

        // cast necessário pois registerStudents aceita Map<ID, Student>
        Map<domain.primitive.ID, Student> studentMap = new LinkedHashMap<>(students);
        course = course.registerStudents(studentMap);

        return course;
    }
}
