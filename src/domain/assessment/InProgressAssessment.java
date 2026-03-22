package domain.assessment;

import domain.primitive.ID;
import domain.primitive.Score;

public class InProgressAssessment implements Assessment {
    private final Assessment assessment;

    public InProgressAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    @Override
    public ID id() {
        return assessment.id();
    }

    @Override
    public String name() {
        return assessment.name();
    }

    @Override
    public Assessment start() {
        throw new IllegalStateException("Assessment is already in progress.");
    }

    @Override
    public Assessment generate_score(Score<Double> score) {
        return new EvaluatedAssessment(new DefaultAssessment(assessment.id(), assessment.name(), score));
    }

    @Override
    public Score<Double> score() {
        return assessment.score();
    }

    @Override
    public String status() {
        return "IN_PROGRESS";
    }
}
