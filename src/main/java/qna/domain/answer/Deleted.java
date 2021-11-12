package qna.domain.answer;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Deleted {
    @Column(nullable = false)
    private boolean deleted;

    public Deleted() {
        this.deleted = false;
    }

    public void setTrue() {
        this.deleted = true;
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