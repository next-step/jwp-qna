package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    private Long createId;

    private Long updateId;

    public LocalDateTime getCreatedAt() {
        return createAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updateAt;
    }

    public Long getCreatedId() {
        return createId;
    }

    public Long getUpdatedId() {
        return updateId;
    }

    public void setCreatedAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setUpdatedAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
