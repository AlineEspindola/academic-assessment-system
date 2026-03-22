package domain.student;

import domain.primitive.ID;

public class NotStartedStudent implements Student {
    private final ID id;
    private final String name;
    private final int registration;

    public NotStartedStudent(ID id, String name, int registration) {
        this.id = id;
        this.name = name;
        this.registration = registration;
    }

    @Override
    public ID id() { return id; }

    @Override
    public String name() { return name; }

    @Override
    public int registration() { return registration; }

    @Override
    public String status() { return "NOT_STARTED"; }

    @Override
    public Student start() {
        return new StudyingStudent(this);
    }

    @Override
    public Student approve() {
        throw new IllegalStateException("Student has not started the course yet.");
    }

    @Override
    public Student fail() {
        throw new IllegalStateException("Student has not started the course yet.");
    }

    @Override
    public Student sendToRecovery() {
        throw new IllegalStateException("Student has not started the course yet.");
    }
}
