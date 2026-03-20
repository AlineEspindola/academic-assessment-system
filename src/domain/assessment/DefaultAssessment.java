package domain.assessment;

import domain.primitive.DefaultScore;
import domain.primitive.ID;
import domain.primitive.IntScore;
import domain.primitive.Score;

public class DefaultAssessment implements Assessment {
    private final ID id;
    private final Score score;

    public DefaultAssessment(ID id, Score score) {
        this.id = id;
        this.score = score;
    }

    public DefaultAssessment(ID id) {
        this.id = id;
        this.score = new IntScore(new DefaultScore("0"));
    }

    @Override
    public ID id() {
        return id;
    }

    @Override
    public Assessment generate_score(Score score) {
        throw new UnsupportedOperationException(
                "DefaultAssessment cannot be modified. Score generation is not allowed for this assessment type."
        );
    }

    @Override
    public Assessment start() {
        return this;
    }

    @Override
    public Score score() {
        return score;
    }
}
