package domain.primitive;

public class MinScore implements Score<Integer> {
    private final Score<Integer> score;
    private final int min;

    public MinScore(Score score, int min) {
        this.score = score;
        this.min = min;
    }

    public MinScore(Score<Integer> score) {
        this.score = score;
        this.min = 0;
    }

    @Override
    public Integer value() {
        int value = score.value();

        if (value < min) {
            return min;
        }

        return value;
    }
}
