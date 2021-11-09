package qna.domain;

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

    @ManyToOne
    @JoinColumn(name = "delete_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleter;

    protected DeleteHistory() {

    }

    public static DeleteHistory question(User deleter, Question question) {
        return of(null, deleter, ContentType.QUESTION, question.getId());
    }

    public static DeleteHistory answer(User deleter, Answer answer) {
        return of(null, deleter, ContentType.ANSWER, answer.getId());
    }

    private static DeleteHistory of(Long id, User deleter, ContentType contentType, Long contentId) {
        DeleteHistory deleteHistory = new DeleteHistory();
        deleteHistory.id = id;
        deleteHistory.contentId = contentId;
        deleteHistory.contentType = contentType;
        deleteHistory.deleter = deleter;
        return deleteHistory;
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
