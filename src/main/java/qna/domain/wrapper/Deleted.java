package qna.domain.wrapper;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deleted {

    @Column(nullable = false)
    private boolean deleted = false;

    public Deleted() { }

    public boolean status() {
        return deleted;
    }

    public void setTrue() {
        deleted = true;
    }

    public void setFalse() {
        deleted = false;
    }

    public void setStatus(boolean deleted) {
        this.deleted = deleted;
    }
}
