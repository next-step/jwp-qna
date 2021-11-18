package qna.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Deleted {

    public static final Deleted FALSE = new Deleted(false);
    public static final Deleted TRUE = new Deleted(true);

    private boolean deleted;

    protected Deleted() {
    }

    public Deleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Deleted deleted1 = (Deleted) o;
        return isDeleted() == deleted1.isDeleted();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isDeleted());
    }
}
