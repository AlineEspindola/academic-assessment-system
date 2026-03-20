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
    public Assessment generate_score(Score score) {
        return new EvaluatedAssessment(new DefaultAssessment(assessment.id(), score));
    }

    @Override
    public Assessment start() {
        throw new IllegalStateException("Assessment already started");
    }

    @Override
    public Score score() {
        return assessment.score();
    }
}
