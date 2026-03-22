package domain.primitive;

public class ValidatedScore implements Score<Double> {
    private static final double MIN = 0.0;
    private static final double MAX = 10.0;

    private final double value;

    public ValidatedScore(double value) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException(
                    "Score must be between " + MIN + " and " + MAX + ". Received: " + value
            );
        }
        this.value = value;
    }

    @Override
    public Double value() {
        return value;
    }
}
