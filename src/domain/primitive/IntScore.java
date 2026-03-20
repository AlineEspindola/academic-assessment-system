package domain.primitive;

public class IntScore implements Score<Integer> {
    private final Score<Integer> score;

    public IntScore(Score score) {
        this.score = score;
    }

    @Override
    public Integer value() {
        return score.value();
    }
}
