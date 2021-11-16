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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import qna.QuestionNotFoundException;
import qna.TypeNotFoundException;
import qna.UnAuthorizedException;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "contend_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_question"))
    private Question question;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "delete_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleteByUser;

    public DeleteHistory(ContentType contentType, Question question, User deleteByUser, LocalDateTime createDate) {
        if (Objects.isNull(contentType)) {
            throw new TypeNotFoundException();
        }

        if (Objects.isNull(question)) {
            throw new QuestionNotFoundException();
        }

        if (Objects.isNull(deleteByUser)) {
            throw new UnAuthorizedException();
        }

        this.contentType = contentType;
        this.question = question;
        this.deleteByUser = deleteByUser;
        this.createDate = createDate;
    }

    protected DeleteHistory() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DeleteHistory that = (DeleteHistory)o;
        return Objects.equals(id, that.id) &&
            contentType == that.contentType &&
            Objects.equals(question, that.question) &&
            Objects.equals(deleteByUser, that.deleteByUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, question, deleteByUser);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
            "id=" + id +
            ", contentType=" + contentType +
            ", createDate=" + createDate +
            '}';
    }
}
