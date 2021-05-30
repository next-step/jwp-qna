package qna.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(nullable = false)
    @CreatedDate
    protected LocalDateTime createdAt;

    @LastModifiedBy
    protected LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "BaseEntity{" +
            "createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }
}
