package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@MappedSuperclass
public abstract class  AbstractTimestampEntity {

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
