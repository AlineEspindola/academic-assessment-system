package domain.primitive;

public class DefaultScore implements Score<Double> {
    private final Double value;

    public DefaultScore(Double value) {
        this.value = value;
    }

    @Override
    public Double value() {
        return value;
    }
}
