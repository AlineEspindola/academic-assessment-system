package domain.bimonthly;

import domain.assessment.Assessment;
import domain.primitive.ID;
import domain.primitive.Score;

import java.util.List;

public interface Bimonthly {
    ID id();

    int order();

    Bimonthly start();

    Bimonthly addAssessment(ID studentId, Assessment assessment);

    Bimonthly finish();

    Score<Double> averageFor(ID studentId);

    List<Assessment> assessmentsFor(ID studentId);

    String status();
}
