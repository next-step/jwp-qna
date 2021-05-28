package qna.domain;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class CommonTransactionInfo {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected CommonTransactionInfo() {
    }
}
