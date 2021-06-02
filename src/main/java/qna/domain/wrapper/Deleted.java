package qna.domain.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deleted {

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    public Deleted() {}

    public boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        this.deleted = true;
    }
}
