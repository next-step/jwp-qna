package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import qna.enumType.ContentType;

@Embeddable
public class Answers {
    @OneToMany(
            mappedBy = "question",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Answer> answers;

    public Answers() {
        answers = new ArrayList<>();
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addDeleteHistory(List<DeleteHistory> deleteHistories, User loginUser) {
        answers.stream()
                .peek(Answer::setDeleted)
                .map(answer -> new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser))
                .forEach(deleteHistories::add);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public int size() {
        return answers.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answers answers1 = (Answers) o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
