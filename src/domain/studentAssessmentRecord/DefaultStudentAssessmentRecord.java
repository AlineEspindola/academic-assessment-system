package domain.studentAssessmentRecord;

import domain.primitive.ID;

import java.time.LocalDate;
import java.time.LocalTime;

public class DefaultStudentAssessmentRecord implements StudentAssessmentRecord {
    private final LocalDate date;
    private final LocalTime time;
    private final ID student_id;
    private final ID assessment_id;

    public DefaultStudentAssessmentRecord(LocalDate date, LocalTime time, ID student_id, ID assessment_id) {
        this.date = date;
        this.time = time;
        this.student_id = student_id;
        this.assessment_id = assessment_id;
    }

    @Override
    public ID student_id() {
        return student_id;
    }

    @Override
    public ID assessment_id() {
        return assessment_id;
    }

    @Override
    public LocalDate date() {
        return date;
    }

    @Override
    public LocalTime time() {
        return time;
    }
}
