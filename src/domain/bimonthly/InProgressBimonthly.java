package domain.bimonthly;

import domain.primitive.ID;
import domain.primitive.Score;
import domain.studentAssessmentRecord.StudentAssessmentRecord;

import java.util.Map;

public class InProgressBimonthly implements Bimonthly{
    @Override
    public void add_student_assessment_records(Map<ID, StudentAssessmentRecord> student_assessment_records) {

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
