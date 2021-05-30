package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Where;

import qna.CannotDeleteException;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    @Where(clause = "deleted = false")
    private List<Answer> answers = new ArrayList<>();

    public Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<DeleteHistory> deleteAllByOwner(User owner) {
        return null;
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(this.answers);
    }
}
