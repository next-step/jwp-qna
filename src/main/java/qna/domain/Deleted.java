package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Objects;

@Embeddable
public class Deleted {

    @Column(nullable = false)
    private boolean deleted = false;

    protected Deleted() {
    }

    public Deleted(boolean deleted) { //TODO: 컨텐츠의 역할이 흐릿함.. 비즈니스가 추가된 후에 검증로직 추가해야 할듯
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete(){
        this.deleted = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deleted deleted1 = (Deleted) o;
        return deleted == deleted1.deleted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleted);
    }
}
