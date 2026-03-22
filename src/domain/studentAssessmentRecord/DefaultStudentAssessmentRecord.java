package domain.studentAssessmentRecord;

import domain.primitive.ID;

import java.time.LocalDate;
import java.time.LocalTime;

public class DefaultStudentAssessmentRecord implements StudentAssessmentRecord {
    private final ID studentId;
    private final ID assessmentId;
    private final LocalDate date;
    private final LocalTime time;

    public DefaultStudentAssessmentRecord(ID studentId, ID assessmentId, LocalDate date, LocalTime time) {
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.date = date;
        this.time = time;
    }

    @Override
    public ID studentId() {
        return studentId;
    }

    @Override
    public ID assessmentId() {
        return assessmentId;
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
