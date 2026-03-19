package domain.assessment;

import domain.primitive.ID;
import domain.primitive.Score;

public interface Assessment {
    ID id();

    Assessment generate_score(Score score);

    Score score();
}
