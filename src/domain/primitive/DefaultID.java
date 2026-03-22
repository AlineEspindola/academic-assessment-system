package domain.primitive;

import java.util.Objects;

public class DefaultID implements ID {
    private final String id;

    public DefaultID(String id) {
        this.id = id;
    }

    @Override
    public String value() {
        return id;
    }

    @Override
    public boolean same(ID id) {
        return this.id.equals(id.value());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ID other)) return false;
        return this.id.equals(other.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
