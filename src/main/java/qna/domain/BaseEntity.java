package qna.domain;

import java.time.*;

import javax.persistence.*;

import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.*;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
