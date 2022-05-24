package qna.domain;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseWriterEntity extends BaseEntity {
    protected Long writerId;

    public Long getWriterId() {
        return writerId;
    }
}
