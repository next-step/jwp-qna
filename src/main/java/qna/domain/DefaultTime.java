package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.*;

@Embeddable
public class DefaultTime {

    @Column(nullable = false, updatable = false)
    private final LocalDateTime createdAt = now();
    private LocalDateTime updatedAt = now();

    public DefaultTime() {
    }

    public void update() {
        this.updatedAt = now();
    }
}
