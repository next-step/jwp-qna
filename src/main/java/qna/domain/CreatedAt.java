package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.data.annotation.CreatedDate;

@Embeddable
public class CreatedAt {
    private static final int MAX_LENGTH = 6;
    private static final String INVALID_MESSAGE = "비어있지 않은 생성일자를 입력해주세요.";
    @CreatedDate
    @Column(nullable = false, length = MAX_LENGTH)
    private LocalDateTime createdAt;

    public CreatedAt(LocalDateTime createdAt) {
        validateCreatedAt(createdAt);
        this.createdAt = createdAt;
    }

    private void validateCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException(INVALID_MESSAGE);
        }
    }

    protected CreatedAt() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreatedAt)) {
            return false;
        }
        CreatedAt createdAt1 = (CreatedAt)o;
        return Objects.equals(createdAt, createdAt1.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt);
    }

    public boolean isAfterOrEqualTo(CreatedAt createdAt) {
        return this.createdAt.isAfter(createdAt.createdAt) || this.createdAt.isEqual(createdAt.createdAt);
    }
}
