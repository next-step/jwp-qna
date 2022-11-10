package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.*;

@MappedSuperclass
public abstract class DefaultTime {

    @Column(nullable = false, updatable = false)
    private final LocalDateTime createdAt = now();
    private LocalDateTime updatedAt = now();

    public void update() {
        this.updatedAt = now();
    }
}
