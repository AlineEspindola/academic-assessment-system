package domain.student;

import domain.assessment.Assessment;
import domain.primitive.ID;

public class ApprovedStudent implements Student {
    private final Student student;

    public ApprovedStudent(Student student) {
        this.student = student;
    }

    @Override
    public ID id() {
        return student.id();
    }

    @Override
    public void submitAssessment(Assessment assessment) {
        throw new UnsupportedOperationException("Approved students cannot submit assessments.");
    }

    @Override
    public String name() {
        return student.name();
    }

    @Override
    public int registration() {
        return student.registration();
    }
}
