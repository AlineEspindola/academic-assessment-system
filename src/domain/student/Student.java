package domain.student;

import domain.assessment.Assessment;
import domain.primitive.ID;

public interface Student {
    ID id();

    void submitAssessment(Assessment assessment);
}
