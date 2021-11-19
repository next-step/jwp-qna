package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deleted {
    public static final boolean NOT = false;
    public static final boolean DELETE = true;

    @Column(nullable = false)
    private final boolean deleted;

    public Deleted() {
        this.deleted = NOT;
    }

    public Deleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Deleted delete() {
        return new Deleted(DELETE);
    }

    public boolean isDeleted() {
        return this.deleted;
    }
}
