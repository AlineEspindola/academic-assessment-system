package domain.semester;

import domain.assessment.Assessment;
import domain.bimonthly.Bimonthly;
import domain.primitive.ID;
import domain.primitive.Score;

public interface Semester {
    ID id();

    Bimonthly firstBimonthly();

    Bimonthly secondBimonthly();

    Semester start();

    Semester finishFirstBimonthly();

    Semester finishSecondBimonthly();

    Semester finish();

    Semester addAssessment(int bimonthlyOrder, ID studentId, Assessment assessment);

    Score<Double> semesterAverage(ID studentId);

    boolean studentEligibleForRecovery(ID studentId);

    String status();
}
