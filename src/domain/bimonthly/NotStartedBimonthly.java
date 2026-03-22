package domain.bimonthly;

import domain.assessment.Assessment;
import domain.primitive.DefaultScore;
import domain.primitive.ID;
import domain.primitive.Score;

import java.time.LocalDate;
import java.util.List;

public class NotStartedBimonthly implements Bimonthly {
    private final ID id;
    private final int order;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public NotStartedBimonthly(ID id, int order, LocalDate startDate, LocalDate endDate) {
        if (order != 1 && order != 2) {
            throw new IllegalArgumentException("Bimonthly order must be 1 or 2.");
        }
        this.id = id;
        this.order = order;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public ID id() { return id; }

    @Override
    public int order() { return order; }

    @Override
    public Bimonthly start() {
        return new InProgressBimonthly(id, order, startDate, endDate);
    }

    @Override
    public Bimonthly addAssessment(ID studentId, Assessment assessment) {
        throw new IllegalStateException("Bimonthly #" + order + " has not started yet.");
    }

    @Override
    public Bimonthly finish() {
        throw new IllegalStateException("Bimonthly #" + order + " has not started yet.");
    }

    @Override
    public Score<Double> averageFor(ID studentId) {
        throw new IllegalStateException("Bimonthly #" + order + " has not started yet — no scores available.");
    }

    @Override
    public List<Assessment> assessmentsFor(ID studentId) {
        throw new IllegalStateException("Bimonthly #" + order + " has not started yet.");
    }

    @Override
    public String status() { return "NOT_STARTED"; }
}
