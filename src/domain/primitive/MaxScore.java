package domain.primitive;

public class MaxScore implements Score<Integer> {
    private final Score<Integer> score;
    private final int max;

    public MaxScore(Score<Integer> score, int max) {
        this.score = score;
        this.max = max;
    }

    public MaxScore(Score<Integer> score) {
        this.score = score;
        this.max = 10;
    }

    @Override
    public Integer value() {
        int value = score.value();

        if (value > max) {
            return max;
        }

        return value;
    }
}