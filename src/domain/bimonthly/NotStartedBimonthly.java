package domain.bimonthly;

import domain.primitive.ID;
import domain.primitive.Score;
import domain.studentAssessmentRecord.StudentAssessmentRecord;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class NotStartedBimonthly implements Bimonthly {
    private final ID id;
    private final LocalDate start_date;
    private final LocalDate end_date;
    private Map<ID, StudentAssessmentRecord> student_assessment_records;

    public NotStartedBimonthly(ID id, LocalDate start_date, LocalDate end_date) {
        this.id = id;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    @Override
    public void add_student_assessment_records(Map<ID, StudentAssessmentRecord> student_assessment_records) {
//        this.student_assessment_records.putAll(student_assessment_records);
    }

    @Override
    public Bimonthly start() {
        return null;
    }

    @Override
    public Bimonthly finish() {
        return null;
    }

    @Override
    public Score final_average(ID student_id) {
        return null;
    }
}
