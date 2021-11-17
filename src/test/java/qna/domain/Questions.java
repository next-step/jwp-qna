package qna.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Questions {
    private final Set<Question> questions;

    public Questions(List<Question> questions) {
        this.questions = new HashSet<>(questions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Questions questions1 = (Questions)o;
        return Objects.equals(questions, questions1.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questions);
    }
}
