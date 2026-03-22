package domain.teacher;

import domain.assessment.Assessment;
import domain.primitive.ID;

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
    public Assessment evaluate_assessment(Assessment assessment) {
        throw new IllegalStateException(
                "Idle teacher cannot evaluate assessments because they are not currently teaching a course."
        );
    }

    @Override
    public String status() {
        return "This professor is not teaching any course";
    }
}
