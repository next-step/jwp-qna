package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Embeddable;

@Embeddable
public class Answers {

    private final Set<Answer> answers = new HashSet<>();

    protected Answers() {
    }

    public void add(final Answer answer) {
        this.answers.add(answer);
    }

    public void deleteAllBy(User writer) {
        this.answers.forEach(answer -> answer.delete(writer));
    }

    public List<Answer> toList() {
        return Collections.unmodifiableList(new ArrayList<>(this.answers));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answers)) {
            return false;
        }
        final Answers that = (Answers)o;
        return Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
