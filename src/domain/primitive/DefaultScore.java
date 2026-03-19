package domain.primitive;

// Criar novos tipos realmente validados
public class DefaultScore implements Score {
    private final String value;

    public DefaultScore(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
