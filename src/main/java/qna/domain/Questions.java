package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Questions {

    @OneToMany(mappedBy = "writer")
    List<Question> questions = new ArrayList<>();

    public Questions() {
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questions questions1 = (Questions) o;
        return Objects.equals(questions, questions1.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questions);
    }
}
