package qna.domain;

import java.io.Serializable;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Deleted deleted1 = (Deleted) o;
        return deleted == deleted1.deleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleted);
    }
}
