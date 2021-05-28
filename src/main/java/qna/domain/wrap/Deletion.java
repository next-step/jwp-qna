package qna.domain.wrap;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Deletion {
    @Column(nullable = false)
    private boolean deleted;

    protected Deletion() {
    }

    public Deletion(boolean deleted) {
        this.deleted = deleted;
    }

    public Deletion delete() {
        if(isDeleted()) {
            throw new IllegalStateException("이미 삭제가 되어있습니다.");
        }

        return new Deletion(true);
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
