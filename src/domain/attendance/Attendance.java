package domain.attendance;

import domain.primitive.ID;

public interface Attendance {
    ID studentId();

    int totalClasses();

    int attendedClasses();

    double attendanceRate();

    boolean meetsMinimumRequirement();

    Attendance registerPresence();

    Attendance registerAbsence();
}
