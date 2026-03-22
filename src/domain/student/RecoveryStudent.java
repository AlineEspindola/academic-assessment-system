package domain.student;

import domain.primitive.ID;

public class RecoveryStudent implements Student {
    private final Student student;

    public RecoveryStudent(Student student) {
        this.student = student;
    }

    @Override
    public ID id() { return student.id(); }

    @Override
    public String name() { return student.name(); }

    @Override
    public int registration() { return student.registration(); }

    @Override
    public String status() { return "IN_RECOVERY"; }

    @Override
    public Student start() {
        throw new IllegalStateException("Student is already in recovery.");
    }

    @Override
    public Student approve() {
        return new ApprovedStudent(student);
    }

    @Override
    public Student fail() {
        return new FailedStudent(student);
    }

    @Override
    public Student sendToRecovery() {
        throw new IllegalStateException("Student is already in recovery.");
    }
}
