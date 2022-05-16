package qna.domain;

import javax.persistence.Entity;

@Entity
public class AnswerDeleteHistory extends DeleteHistory {

    protected AnswerDeleteHistory() {
    }

    public AnswerDeleteHistory(Answer answer) {
        super(ContentType.ANSWER, answer.getId(), answer.getWriterId());
    }
}
