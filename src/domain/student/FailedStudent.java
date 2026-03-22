package domain.student;

import domain.primitive.ID;

public class FailedStudent implements Student {
    private final Student student;

    public FailedStudent(Student student) {
        this.student = student;
    }

    @Override
    public ID id() { return student.id(); }

    @Override
    public String name() { return student.name(); }

    @Override
    public int registration() { return student.registration(); }

    @Override
    public String status() { return "FAILED"; }

    @Override
    public Student start() {
        throw new UnsupportedOperationException("Failed students cannot restart the course.");
    }

    @Override
    public Student approve() {
        throw new UnsupportedOperationException("Failed students cannot be approved.");
    }

    @Override
    public Student fail() {
        throw new UnsupportedOperationException("Student has already failed.");
    }

    @Override
    public Student sendToRecovery() {
        throw new UnsupportedOperationException("Failed students cannot go to recovery.");
    }
}
