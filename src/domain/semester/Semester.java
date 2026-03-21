package domain.semester;

import domain.primitive.ID;

public interface Semester {
    ID id();

    void start();

    void finish();
}
