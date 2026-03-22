package domain.semester;

import domain.bimonthly.Bimonthly;
import domain.primitive.ID;
import domain.primitive.Score;

import java.time.LocalDate;

public class NotStartedSemester implements Semester {
    private final ID id;
    private final LocalDate start_date;
    private final LocalDate end_date;
    private final Bimonthly first_bimonthly;
    private final Bimonthly last_bimonthly;

    public NotStartedSemester(ID id, LocalDate start_date, LocalDate end_date, Bimonthly first_bimonthly, Bimonthly last_bimonthly) {
        this.id = id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.first_bimonthly = first_bimonthly;
        this.last_bimonthly = last_bimonthly;
    }

    @Override
    public ID id() {
        return id;
    }

    @Override
    public void start() {

    }

    @Override
    public void finish() {

    }

    @Override
    public void finish_first_bimonthly() {

    }

    @Override
    public void finish_last_bimonthly() {

    }

    @Override
    public LocalDate start_date() {
        return start_date;
    }

    @Override
    public LocalDate end_date() {
        return null;
    }

    @Override
    public Score final_average() {
        return null;
    }
}
