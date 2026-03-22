package domain.teacher;

import domain.assessment.Assessment;
import domain.primitive.ID;

public class TeachingTeacher implements Teacher {
    private final Teacher teacher;

    public TeachingTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public ID id() {
        return teacher.id();
    }

    @Override
    public String name() {
        return teacher.name();
    }

    @Override
    public Assessment evaluate_assessment(Assessment assessment) {
        return null;
    }

    @Override
    public String status() {
        return "This teacher is teaching a course";
    }
}
