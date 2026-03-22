package domain.bimonthly;

import domain.primitive.ID;
import domain.primitive.Score;
import domain.studentAssessmentRecord.StudentAssessmentRecord;

import java.util.Map;

public interface Bimonthly {
    void add_student_assessment_records(Map<ID, StudentAssessmentRecord> student_assessment_records);

    Bimonthly start();

    Bimonthly finish();

    Score final_average(ID student_id);
}
