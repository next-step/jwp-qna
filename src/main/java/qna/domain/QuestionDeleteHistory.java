package qna.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "question_delete_history")
@DiscriminatorValue(ContentType.Values.ANSWER)
public class QuestionDeleteHistory extends DeleteHistory {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, updatable = false)
    private Question question;

    protected QuestionDeleteHistory() {
    }

    public QuestionDeleteHistory(Question question) {
        super(question.getId(), question.getWriter());
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuestionDeleteHistory that = (QuestionDeleteHistory) o;
        return Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), question.getId());
    }
}
