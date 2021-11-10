package qna.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public class BaseEntity {
    @Column(name ="created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @Column(name ="updated_at")
    protected LocalDateTime updatedAt;

    @PrePersist
    private void onCreateForBaseEntity() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdateForBaseEntity() {
        this.updatedAt = LocalDateTime.now();
    }
}
