package domain.semester;

import domain.assessment.Assessment;
import domain.bimonthly.Bimonthly;
import domain.primitive.DefaultScore;
import domain.primitive.ID;
import domain.primitive.Score;

import java.time.LocalDate;

public class FinishedSemester implements Semester {
    private static final double DIRECT_APPROVAL_MIN = 6.0;
    private static final double RECOVERY_ELIGIBLE_MIN = 4.0;
    private static final double RECOVERY_ELIGIBLE_MAX = 6.0;

    private final ID id;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Bimonthly firstBimonthly;
    private final Bimonthly secondBimonthly;

    public FinishedSemester(
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
        throw new IllegalStateException("Semester is already finished.");
    }

    @Override
    public Semester finishFirstBimonthly() {
        throw new IllegalStateException("Semester is already finished.");
    }

    @Override
    public Semester finishSecondBimonthly() {
        throw new IllegalStateException("Semester is already finished.");
    }

    @Override
    public Semester finish() {
        throw new IllegalStateException("Semester is already finished.");
    }

    @Override
    public Semester addAssessment(int bimonthlyOrder, ID studentId, Assessment assessment) {
        throw new IllegalStateException("Semester is already finished — cannot add assessments.");
    }

    @Override
    public Score<Double> semesterAverage(ID studentId) {
        double n1 = firstBimonthly.averageFor(studentId).value();
        double n2 = secondBimonthly.averageFor(studentId).value();
        return new DefaultScore((n1 + n2) / 2.0);
    }

    /**
     * Regra do enunciado:
     * Elegível para recuperação se: 4.0 <= M < 6.0
     * (a frequência mínima é verificada no Course/View com o objeto Attendance)
     */
    @Override
    public boolean studentEligibleForRecovery(ID studentId) {
        double averageStudent = semesterAverage(studentId).value();
        return averageStudent >= RECOVERY_ELIGIBLE_MIN && averageStudent < RECOVERY_ELIGIBLE_MAX;
    }

    /**
     * Aprovado diretamente se M >= 6.0
     */
    public boolean studentApprovedDirect(ID studentId) {
        return semesterAverage(studentId).value() >= DIRECT_APPROVAL_MIN;
    }

    /**
     * Calcula a média final com nota de recuperação.
     * R substitui a menor nota entre N1 e N2:
     * Mfinal = (max(N1, N2) + R) / 2
     */
    public Score<Double> finalAverageWithRecovery(ID studentId, Score<Double> recoveryScore) {
        double n1 = firstBimonthly.averageFor(studentId).value();
        double n2 = secondBimonthly.averageFor(studentId).value();
        double highest = Math.max(n1, n2);
        return new DefaultScore((highest + recoveryScore.value()) / 2.0);
    }

    /**
     * Aprovado após recuperação se Mfinal >= 6.0
     */
    public boolean studentApprovedAfterRecovery(ID studentId, Score<Double> recoveryScore) {
        return finalAverageWithRecovery(studentId, recoveryScore).value() >= DIRECT_APPROVAL_MIN;
    }

    @Override
    public String status() { return "FINISHED"; }
}
