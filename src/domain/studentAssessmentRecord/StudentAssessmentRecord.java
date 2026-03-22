package domain.studentAssessmentRecord;

import domain.primitive.ID;

import java.time.LocalDate;
import java.time.LocalTime;

public interface StudentAssessmentRecord {
    ID student_id();

    ID assessment_id();

    LocalDate date();

    LocalTime time();
}
