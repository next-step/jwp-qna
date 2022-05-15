package qna.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @Column(name = "REG_DTS", nullable = false, updatable = false)
    private LocalDateTime regDts;

    @LastModifiedDate
    @Column(name = "MOD_DTS")
    private LocalDateTime modDts;

    public LocalDateTime getRegDts() {
        return regDts;
    }

    public LocalDateTime getModDts() {
        return modDts;
    }
}
