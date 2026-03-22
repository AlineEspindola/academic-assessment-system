package domain.assessment;

import domain.primitive.DefaultScore;
import domain.primitive.ID;
import domain.primitive.Score;

public class DefaultAssessment implements Assessment {
    private final ID id;
    private final String name;
    private final Score<Double> score;

    public DefaultAssessment(ID id, String name, Score<Double> score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public DefaultAssessment(ID id, String name) {
        this.id = id;
        this.name = name;
        this.score = new DefaultScore(0.0);
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
    public Assessment start() {
        return this;
    }

    @Override
    public Assessment generate_score(Score<Double> score) {
        throw new UnsupportedOperationException(
                "DefaultAssessment cannot be modified. Use start() first."
        );
    }

    @Override
    public Score<Double> score() {
        return score;
    }

    @Override
    public String status() {
        return "NOT_STARTED";
    }
}
