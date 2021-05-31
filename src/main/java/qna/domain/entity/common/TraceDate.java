package qna.domain.entity.common;

import lombok.Getter;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class TraceDate {

    @Column(nullable = false)
    protected LocalDateTime createdAt;

    protected LocalDateTime updatedAt;

    public TraceDate() {
        this.createdAt = LocalDateTime.now();
    }

}
