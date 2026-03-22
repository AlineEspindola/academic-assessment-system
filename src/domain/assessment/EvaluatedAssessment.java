package domain.assessment;

import domain.primitive.ID;
import domain.primitive.Score;

public class EvaluatedAssessment implements Assessment {
    private final Assessment assessment;

    public EvaluatedAssessment(Assessment assessment) {
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
        throw new UnsupportedOperationException("Assessment has already been evaluated and cannot be started again.");
    }

    @Override
    public Assessment generate_score(Score<Double> score) {
        throw new UnsupportedOperationException("Assessment has already been evaluated. Score cannot be changed.");
    }

    @Override
    public Score<Double> score() {
        return assessment.score();
    }

    @Override
    public String status() {
        return "EVALUATED";
    }
}
