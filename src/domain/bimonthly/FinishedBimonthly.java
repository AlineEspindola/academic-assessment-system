package domain.bimonthly;

import domain.assessment.Assessment;
import domain.primitive.DefaultScore;
import domain.primitive.ID;
import domain.primitive.Score;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FinishedBimonthly implements Bimonthly {
    private final ID id;
    private final int order;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Map<String, List<Assessment>> assessmentsByStudent;

    public FinishedBimonthly(
            ID id, int order,
            LocalDate startDate, LocalDate endDate,
            Map<String, List<Assessment>> assessmentsByStudent
    ) {
        this.id = id;
        this.order = order;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assessmentsByStudent = assessmentsByStudent;
    }

    @Override
    public ID id() { return id; }

    @Override
    public int order() { return order; }

    @Override
    public Bimonthly start() {
        throw new IllegalStateException("Bimonthly #" + order + " has already finished.");
    }

    @Override
    public Bimonthly addAssessment(ID studentId, Assessment assessment) {
        throw new IllegalStateException("Bimonthly #" + order + " has already finished — cannot add assessments.");
    }

    @Override
    public Bimonthly finish() {
        throw new IllegalStateException("Bimonthly #" + order + " has already finished.");
    }

    @Override
    public Score<Double> averageFor(ID studentId) {
        List<Assessment> list = assessmentsByStudent.getOrDefault(studentId.value(), List.of());
        if (list.isEmpty()) return new DefaultScore(0.0);
        double sum = list.stream().mapToDouble(a -> a.score().value()).sum();
        return new DefaultScore(sum / list.size());
    }

    @Override
    public List<Assessment> assessmentsFor(ID studentId) {
        return Collections.unmodifiableList(
                assessmentsByStudent.getOrDefault(studentId.value(), List.of())
        );
    }

    @Override
    public String status() { return "FINISHED"; }
}
