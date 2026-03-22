package domain.studentAssessmentRecord;

import domain.primitive.ID;

import java.time.LocalDate;
import java.time.LocalTime;

public interface StudentAssessmentRecord {
    ID studentId();

    ID assessmentId();

    LocalDate date();

    LocalTime time();
}
