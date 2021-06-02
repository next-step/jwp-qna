package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Deletion {

    @Column(nullable = false)
    private boolean deleted = false;

    public Deletion() {
    }

    public Deletion(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        this.deleted = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deletion)) return false;
        Deletion deletion = (Deletion) o;
        return isDeleted() == deletion.isDeleted();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isDeleted());
    }
}
