package domain.teacher;

import domain.assessment.Assessment;
import domain.primitive.ID;
import domain.primitive.Score;

public class IdleTeacher implements Teacher {
    private final ID id;
    private final String name;

    public IdleTeacher(ID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public ID id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Assessment evaluateAssessment(Assessment assessment, Score<Double> score) {
        throw new IllegalStateException(
                "Idle teacher cannot evaluate assessments — not currently teaching a course."
        );
    }

    @Override
    public String status() {
        return "IDLE";
    }
}
