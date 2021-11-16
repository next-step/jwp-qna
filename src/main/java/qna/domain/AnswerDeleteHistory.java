package qna.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "answer_delete_history")
@DiscriminatorValue(ContentType.Values.ANSWER)
public class AnswerDeleteHistory extends DeleteHistory {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false, updatable = false)
    private Answer answer;

    protected AnswerDeleteHistory() {
    }

    public AnswerDeleteHistory(Answer answer) {
        super(answer.getId(), answer.getWriter());
        this.answer = answer;
    }

    public Answer getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AnswerDeleteHistory that = (AnswerDeleteHistory) o;
        return Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        // 순환참조를 막기위해 answer.getId()로 해시함
        return Objects.hash(super.hashCode(), answer.getId());
    }
}
