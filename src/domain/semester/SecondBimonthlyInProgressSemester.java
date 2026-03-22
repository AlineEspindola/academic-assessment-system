package domain.semester;

import domain.assessment.Assessment;
import domain.bimonthly.Bimonthly;
import domain.primitive.DefaultScore;
import domain.primitive.ID;
import domain.primitive.Score;

import java.time.LocalDate;

public class SecondBimonthlyInProgressSemester implements Semester {
    private final ID id;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Bimonthly firstBimonthly;
    private final Bimonthly secondBimonthly;

    public SecondBimonthlyInProgressSemester(
            ID id,
            LocalDate startDate,
            LocalDate endDate,
            Bimonthly firstBimonthly,
            Bimonthly secondBimonthly
    ) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.firstBimonthly = firstBimonthly;
        this.secondBimonthly = secondBimonthly;
    }

    @Override
    public ID id() { return id; }

    @Override
    public Bimonthly firstBimonthly() { return firstBimonthly; }

    @Override
    public Bimonthly secondBimonthly() { return secondBimonthly; }

    @Override
    public Semester start() {
        throw new IllegalStateException("Semester is already in progress.");
    }

    @Override
    public Semester finishFirstBimonthly() {
        throw new IllegalStateException("First bimonthly is already finished.");
    }

    @Override
    public Semester finishSecondBimonthly() {
        Bimonthly finished = secondBimonthly.finish();
        return new FinishedSemester(id, startDate, endDate, firstBimonthly, finished);
    }

    @Override
    public Semester finish() {
        throw new IllegalStateException("Second bimonthly must be finished before finishing the semester.");
    }

    @Override
    public Semester addAssessment(int bimonthlyOrder, ID studentId, Assessment assessment) {
        if (bimonthlyOrder == 2) {
            Bimonthly updated = secondBimonthly.addAssessment(studentId, assessment);
            return new SecondBimonthlyInProgressSemester(id, startDate, endDate, firstBimonthly, updated);
        }
        throw new IllegalStateException("First bimonthly is already finished — cannot add assessments to it.");
    }

    @Override
    public Score<Double> semesterAverage(ID studentId) {
        double n1 = firstBimonthly.averageFor(studentId).value();
        double n2 = secondBimonthly.averageFor(studentId).value();
        return new DefaultScore((n1 + n2) / 2.0);
    }

    @Override
    public boolean studentEligibleForRecovery(ID studentId) {
        throw new IllegalStateException("Semester is not finished yet — recovery eligibility unavailable.");
    }

    @Override
    public String status() { return "SECOND_BIMONTHLY_IN_PROGRESS"; }
}
