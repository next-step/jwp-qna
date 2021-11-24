package qna.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    @Embedded
    private CreatedAt createdAt = new CreatedAt(LocalDateTime.now());

    @LastModifiedDate
    @Column(length = 6)
    private LocalDateTime updatedAt;

    protected BaseTimeEntity() {
    }

    public CreatedAt getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
