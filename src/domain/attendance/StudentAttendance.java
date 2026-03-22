package domain.attendance;

import domain.primitive.ID;

public class StudentAttendance implements Attendance {
    private static final double MINIMUM_RATE = 0.75;

    private final ID studentId;
    private final int totalClasses;
    private final int attendedClasses;

    public StudentAttendance(ID studentId, int totalClasses, int attendedClasses) {
        if (totalClasses < 0) {
            throw new IllegalArgumentException("Total classes cannot be negative.");
        }
        if (attendedClasses < 0 || attendedClasses > totalClasses) {
            throw new IllegalArgumentException(
                    "Attended classes must be between 0 and totalClasses (" + totalClasses + ")."
            );
        }
        this.studentId = studentId;
        this.totalClasses = totalClasses;
        this.attendedClasses = attendedClasses;
    }

    public StudentAttendance(ID studentId) {
        this.studentId = studentId;
        this.totalClasses = 0;
        this.attendedClasses = 0;
    }

    @Override
    public ID studentId() {
        return studentId;
    }

    @Override
    public int totalClasses() {
        return totalClasses;
    }

    @Override
    public int attendedClasses() {
        return attendedClasses;
    }

    @Override
    public double attendanceRate() {
        if (totalClasses == 0) return 0.0;
        return (double) attendedClasses / totalClasses;
    }

    @Override
    public boolean meetsMinimumRequirement() {
        return attendanceRate() >= MINIMUM_RATE;
    }

    @Override
    public Attendance registerPresence() {
        return new StudentAttendance(studentId, totalClasses + 1, attendedClasses + 1);
    }

    @Override
    public Attendance registerAbsence() {
        return new StudentAttendance(studentId, totalClasses + 1, attendedClasses);
    }
}
