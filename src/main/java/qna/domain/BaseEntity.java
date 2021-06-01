package qna.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_at", nullable = false)
    private LocalDateTime  createdAt = LocalDateTime.now();

    @Column(name = "update_at")
    private LocalDateTime  updateAt;
}
