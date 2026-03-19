public class StudyingStudent implements Student {
    private final Student student;

    public StudyingStudent(Student student) {
        this.student = student;
    }

    @Override
    public ID id() {
        return student.id();
    }

    @Override
    public void submitAssessment(Assessment assessment) {

    }
}
