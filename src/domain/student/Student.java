package domain.student;

import domain.assessment.Assessment;
import domain.primitive.ID;

public interface Student {
    ID id();

    String name();

    int registration();

    String status();

    Student start();

    Student approve();

    Student fail();

    Student sendToRecovery();
}
