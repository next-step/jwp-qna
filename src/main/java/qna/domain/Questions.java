package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Questions {
    @OneToMany(mappedBy = "writer")
    private List<Question> questions;

    public Questions() {
        this.questions = new ArrayList<>();
    }

    public List<Question> getQuestion() {
        return Collections.unmodifiableList(questions);
    }

    public void add(final Question question) {
        questions.add(question);
    }

    public boolean contains(final Question question) {
        return questions.contains(question);
    }

    public int size() {
        return questions.size();
    }
    @Override
    public String toString() {
        return "Questions{" +
                "questions=" + questions +
                '}';
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Questions questions1 = (Questions) o;
        return Objects.equals(questions, questions1.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questions);
    }
}
