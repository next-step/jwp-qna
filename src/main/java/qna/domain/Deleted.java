package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Deleted {
    @Column(nullable = false)
    private boolean deleted = false;

    protected Deleted() {
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deleted otherDeleted = (Deleted) o;
        return deleted == otherDeleted.deleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleted);
    }
}
