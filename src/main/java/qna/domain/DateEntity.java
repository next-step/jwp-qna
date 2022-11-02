package qna.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DateEntity extends BaseEntity {

    @Column(nullable = false, updatable = false)
    protected final LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = true)
    protected LocalDateTime updatedAt;
}
