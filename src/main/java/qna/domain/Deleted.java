package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deleted {

    @Column(nullable = false)
    private boolean deleted = false;

    public Deleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Deleted() {

    }

    public boolean isDeleted() {
        return deleted;
    }
}
