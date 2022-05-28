package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    @Column(name = "content_id")
    private Long contentId;
    @ManyToOne
    @JoinColumn(name = "delete_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User remover;
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    private DeleteHistory(ContentType contentType, Long contentId, User remover, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.remover = remover;
        this.createDate = createDate;
    }

    public static DeleteHistory ofAnswer(Long answerId, User remover) {
        return new DeleteHistory(ContentType.ANSWER, answerId, remover, LocalDateTime.now());
    }

    public static DeleteHistory ofQuestion(Long questionId, User remover) {
        return new DeleteHistory(ContentType.QUESTION, questionId, remover, LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
