package qna.domain;

import javax.persistence.Embeddable;

@Embeddable
public class DeleteFlag {

    private boolean deleted;

    protected DeleteFlag() {
    }

    public DeleteFlag(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public static DeleteFlag notDeleted() {
        return new DeleteFlag(false);
    }

    public static DeleteFlag deleted() {
        return new DeleteFlag(true);
    }
}
