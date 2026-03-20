import domain.assessment.Assessment;
import domain.assessment.InProgressAssessment;
import domain.assessment.NotStartedAssessment;
import domain.primitive.*;
import domain.student.NotStartedStudent;
import domain.student.Student;

public class Main {
    public static void main(String[] args) {
        // Testes
        Score<String> defaultScore = new DefaultScore("11");
        Integer parsed = Integer.parseInt(defaultScore.value());

        Score<Integer> intScore = () -> parsed;

        Assessment assessment1 = new NotStartedAssessment(new DefaultID("1"));

        assessment1 = assessment1.start();

        assessment1 = assessment1.generate_score(
                new MinScore(
                        new MaxScore(intScore)
                )
        );

        System.out.println(assessment1.score().value());

    }
}