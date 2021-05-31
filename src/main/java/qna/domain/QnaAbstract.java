package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class QnaAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
}
