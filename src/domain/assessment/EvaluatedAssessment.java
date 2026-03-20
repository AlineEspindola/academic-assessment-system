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
    public Assessment generate_score(Score score) {
        throw new UnsupportedOperationException(
                "Assessment has already been evaluated. Score cannot be generated again."
        );
    }

    @Override
    public Assessment start() {
        throw new UnsupportedOperationException(
                "Assessment has already been completed and cannot be started again."
        );
    }

    @Override
    public Score score() {
        return assessment.score();
    }
}
