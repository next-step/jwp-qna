package qna.domain;

import javax.persistence.Entity;

@Entity
public class QuestionDeleteHistory extends DeleteHistory {

    protected QuestionDeleteHistory() {
    }

    public QuestionDeleteHistory(Question question) {
        super(ContentType.QUESTION, question.getId(), question.getWriterId());
    }
}
