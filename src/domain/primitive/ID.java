package domain.primitive;

public interface ID {
    String value();

    boolean same(ID id);
}
