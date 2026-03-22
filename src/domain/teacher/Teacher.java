package domain.teacher;

import domain.assessment.Assessment;
import domain.primitive.ID;

public interface Teacher {
    ID id();

    String name();

    Assessment evaluate_assessment(Assessment assessment);

    String status();
}
