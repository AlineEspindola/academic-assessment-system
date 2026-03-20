package domain.primitive;

public class DefaultID implements ID {
    private final String id;

    public DefaultID(String id) {
        this.id = id;
    }

    @Override
    public String value() {
        return id;
    }
}
