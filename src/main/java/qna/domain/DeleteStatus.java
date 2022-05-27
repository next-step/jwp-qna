package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class DeleteStatus {
    @Column(nullable = false)
    private boolean deleted = false;

    public DeleteStatus() {
    }

    public void delete() {
        this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteStatus that = (DeleteStatus) o;
        return deleted == that.deleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleted);
    }

    @Override
    public String toString() {
        return "DeleteStatus{" +
                "deleted=" + deleted +
                '}';
    }
}
