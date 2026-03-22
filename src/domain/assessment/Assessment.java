package domain.assessment;

import domain.primitive.ID;
import domain.primitive.Score;

public interface Assessment {
    ID id();

    String name();

    Assessment start();

    Assessment generate_score(Score<Double> score);

    Score<Double> score();

    String status();
}
