package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedBy
    private LocalDateTime updatedAt;

    protected Long creatorId;

    protected Long lastModifierId;

    public Long getId() {
        return id;
    }

    public Long getCreatorId() {
        return creatorId;
    }
}
