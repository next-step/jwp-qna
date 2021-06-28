package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deletion {

    @Column(nullable = false)
    private Boolean deleted;

    public Deletion() {
        this.deleted = false;
    }

    public DeleteHistory delete(Object content) {
        this.deleted = true;
        return DeleteHistory.addHistory(content);
    }

    public Boolean isDeleted(){
        return this.deleted;
    }
}
