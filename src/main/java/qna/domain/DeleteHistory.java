package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "delete_history")
public class DeleteHistory extends BaseEntity {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleter;

    protected DeleteHistory() {

    }

    private DeleteHistory(Long id, Long contentId, ContentType contentType, User deleter) {
        this.id = id;
        this.contentId = contentId;
        this.contentType = contentType;
        this.deleter = deleter;
    }

    public static DeleteHistory question(User deleter, Question question) {
        return of(null, deleter, ContentType.QUESTION, question.getId());
    }

    public static DeleteHistory answer(User deleter, Answer answer) {
        return of(null, deleter, ContentType.ANSWER, answer.getId());
    }

    private static DeleteHistory of(Long id, User deleter, ContentType contentType, Long contentId) {
        return new DeleteHistory(id, contentId, contentType, deleter);
    }

    public Long getId() {
        return id;
    }

    public Long getContentId() {
        return contentId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public User getDeleter() {
        return deleter;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        DeleteHistory that = (DeleteHistory)obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
