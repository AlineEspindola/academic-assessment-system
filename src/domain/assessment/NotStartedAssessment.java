package domain.assessment;

import domain.primitive.DefaultScore;
import domain.primitive.ID;
import domain.primitive.Score;

public class NotStartedAssessment implements Assessment {
    private final ID id;
    private final Score score;

    public NotStartedAssessment(ID id) {
        this.id = id;
        this.score = new DefaultScore("0");
    }

    public NotStartedAssessment(ID id, Score score) {
        this.id = id;
        this.score = score;
    }

    @Override
    public ID id() {
        return id;
    }

    @Override
    public Assessment generate_score(Score score) {
        return new NotStartedAssessment(this.id, score);
    }

    @Override
    public Score score() {
        return score;
    }
}
