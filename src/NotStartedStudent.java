public class NotStartedStudent implements Student {
    private final ID id;

    public NotStartedStudent(ID id) {
        this.id = id;
    }

    @Override
    public ID id() {
        return id;
    }

    @Override
    public void submitAssessment(Assessment assessment) {
        throw new UnsupportedOperationException("Students who have not started cannot submit assessments.");
    }
}
