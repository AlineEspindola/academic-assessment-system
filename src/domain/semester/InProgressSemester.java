package domain.semester;

import domain.assessment.Assessment;
import domain.bimonthly.Bimonthly;
import domain.primitive.DefaultScore;
import domain.primitive.ID;
import domain.primitive.Score;

import java.time.LocalDate;

public class InProgressSemester implements Semester {
    private final ID id;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Bimonthly firstBimonthly;
    private final Bimonthly secondBimonthly;

    public InProgressSemester(
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
        Bimonthly finished = firstBimonthly.finish();
        Bimonthly startedSecond = secondBimonthly.start();
        return new SecondBimonthlyInProgressSemester(id, startDate, endDate, finished, startedSecond);
    }

    @Override
    public Semester finishSecondBimonthly() {
        throw new IllegalStateException("First bimonthly must be finished before the second can end.");
    }

    @Override
    public Semester finish() {
        throw new IllegalStateException("Both bimonthly periods must be completed before finishing the semester.");
    }

    @Override
    public Semester addAssessment(int bimonthlyOrder, ID studentId, Assessment assessment) {
        if (bimonthlyOrder == 1) {
            Bimonthly updated = firstBimonthly.addAssessment(studentId, assessment);
            return new InProgressSemester(id, startDate, endDate, updated, secondBimonthly);
        }
        throw new IllegalStateException("Second bimonthly has not started yet.");
    }

    @Override
    public Score<Double> semesterAverage(ID studentId) {
        double n1 = firstBimonthly.averageFor(studentId).value();
        return new DefaultScore(n1);
    }

    @Override
    public boolean studentEligibleForRecovery(ID studentId) {
        throw new IllegalStateException("Semester is not finished yet — recovery eligibility unavailable.");
    }

    @Override
    public String status() { return "FIRST_BIMONTHLY_IN_PROGRESS"; }
}
