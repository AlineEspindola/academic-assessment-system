package domain.student;

import domain.assessment.Assessment;
import domain.primitive.ID;

public class FailedStudent implements Student {
    private final Student student;

    public FailedStudent(Student student) {
        this.student = student;
    }

    @Override
    public ID id() {
        return student.id();
    }

    @Override
    public void submitAssessment(Assessment assessment) {

    }

    @Override
    public String name() {
        return student.name();
    }

    @Override
    public int registration() {
        return student.registration();
    }

    @Override
    public String status() {
        return "This student failed.";
    }
}
