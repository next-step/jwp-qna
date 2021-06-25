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

    public void delete() {
        this.deleted = true;
    }

    public Boolean isDeleted(){
        return this.deleted;
    }
}
