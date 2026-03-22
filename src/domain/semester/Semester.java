package domain.semester;

import domain.primitive.ID;
import domain.primitive.Score;

import java.time.LocalDate;

public interface Semester {
    ID id();

    void start();

    void finish();

    void finish_first_bimonthly();

    void finish_last_bimonthly();

    LocalDate start_date();

    LocalDate end_date();

    Score final_average();
}
