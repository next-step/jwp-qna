package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id")
    private User deletedByUser;

    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser) {
        this(contentType, contentId, deletedByUser, LocalDateTime.now());
    }

    private DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id)
                && Objects.equals(contentId, that.contentId)
                && contentType == that.contentType
                && Objects.equals(deletedByUser, that.deletedByUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentId, contentType, deletedByUser);
    }

    public static DeleteHistory ofAnswer(Long contentId, User deletedByUser) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deletedByUser);
    }

    public static DeleteHistory ofQuestion(Long contentId, User deletedByUser) {
        return new DeleteHistory(ContentType.QUESTION, contentId, deletedByUser);
    }
}
