package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class CommonTransactionInfo {

    @Column(nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    protected CommonTransactionInfo() {
    }

    public void update() {
        updatedAt = LocalDateTime.now();
    }
}
