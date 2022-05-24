package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private ContentType contentType;

    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "delete_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User remover;

    @CreatedDate
    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User remover) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.remover = remover;
        this.createDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeleteHistory that = (DeleteHistory) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", remover=" + remover +
                ", createDate=" + createDate +
                '}';
    }
}
