public interface Assessment {
    ID id();

    Assessment generate_score(Score score);
}
