package domain.teacher;

import domain.assessment.Assessment;
import domain.primitive.ID;
import domain.primitive.Score;

public interface Teacher {
    ID id();

    String name();

    Assessment evaluateAssessment(Assessment assessment, Score<Double> score);

    String status();
}
