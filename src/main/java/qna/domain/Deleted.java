package qna.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deleted implements Serializable {

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    protected Deleted() {
    }

    public DeleteHistory deleteOf(Question question) {
        this.deleted = true;
        return DeleteHistory.OfQuestion(question);
    }

    public DeleteHistory deleteOf(Answer answer) {
        this.deleted = true;
        return DeleteHistory.OfAnswer(answer);
    }

    public boolean isDeleted() {
        return deleted;
    }

}
