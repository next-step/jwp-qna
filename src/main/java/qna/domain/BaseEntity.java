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
    protected LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    protected LocalDateTime modifiedDate;
}
