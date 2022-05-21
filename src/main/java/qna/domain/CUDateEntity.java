package qna.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class CUDateEntity {
    @Column(name = "CREATED_AT" , nullable = false)
    final LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UPDATED_AT")
    LocalDateTime updatedAt;
}
