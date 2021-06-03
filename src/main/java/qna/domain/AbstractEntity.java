package qna.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected LocalDateTime createAt = LocalDateTime.now();

    @Column
    protected LocalDateTime updateAt = LocalDateTime.now();

    public AbstractEntity() {
    }

    public AbstractEntity(Long id) {
        this.id = id;
    }

    public AbstractEntity(Long id, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
