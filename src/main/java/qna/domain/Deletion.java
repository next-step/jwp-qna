package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Deletion {

    @Column(nullable = false, name = "deleted")
    private boolean deletion;

    protected Deletion() {
        this.deletion = false;
    }

    public Deletion(boolean deletion) {
        this.deletion = deletion;
    }

    public boolean isDeleted() {
        return this.deletion;
    }

    public void delete() throws CannotDeleteException {
        if (isDeleted()) {
            throw new CannotDeleteException("이미 삭제된 데이터 입니다.");
        }
        this.deletion = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deletion deletion1 = (Deletion) o;
        return deletion == deletion1.deletion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deletion);
    }

    @Override
    public String toString() {
        return "Deletion{" +
                "deletion=" + deletion +
                '}';
    }
}
