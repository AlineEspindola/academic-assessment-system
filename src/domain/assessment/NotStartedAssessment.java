package domain.assessment;

import domain.primitive.DefaultScore;
import domain.primitive.ID;
import domain.primitive.Score;

public class NotStartedAssessment implements Assessment {
    private final Assessment assessment;

    public NotStartedAssessment(ID id, String name) {
        this.assessment = new DefaultAssessment(id, name);
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
        return new InProgressAssessment(assessment);
    }

    @Override
    public Assessment generate_score(Score<Double> score) {
        throw new IllegalStateException("Cannot generate score: assessment not started yet.");
    }

    @Override
    public Score<Double> score() {
        return assessment.score();
    }

    @Override
    public String status() {
        return "NOT_STARTED";
    }
}
