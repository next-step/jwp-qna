package qna.domain.wrap;

import java.util.Objects;

public class Deletion {
    private boolean deleted;

    public Deletion(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deletion deletion = (Deletion) o;
        return deleted == deletion.deleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleted);
    }
}
