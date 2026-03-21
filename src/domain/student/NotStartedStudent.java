package domain.student;

import domain.assessment.Assessment;
import domain.primitive.ID;

public class NotStartedStudent implements Student {
    private final ID id;
    private final String name;
    private final int registration;

    public NotStartedStudent(ID id, String name, int registration) {
        this.id = id;
        this.name = name;
        this.registration = registration;
    }

    @Override
    public ID id() {
        return id;
    }

    @Override
    public void submitAssessment(Assessment assessment) {
        throw new UnsupportedOperationException("Students who have not started cannot submit assessments.");
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int registration() {
        return registration;
    }
}
