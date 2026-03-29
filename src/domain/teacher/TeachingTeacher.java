package domain.teacher;

import domain.assessment.Assessment;
import domain.primitive.ID;
import domain.primitive.Score;

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
    public Assessment evaluateAssessment(Assessment assessment, Score<Double> score) {
        Assessment assessmentInProgress = assessment.start();
        return assessmentInProgress.generate_score(score);
    }

    @Override
    public String status() {
        return "TEACHING";
    }
}
