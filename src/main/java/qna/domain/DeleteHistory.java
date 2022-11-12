package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
@EntityListeners(AuditingEntityListener.class)
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", length = 255)
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    @JoinColumn(name = "deleted_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User deletedByUser;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createDate;

    public static DeleteHistory fromQuestion(Question question) {
        return new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now());
    }

    public static DeleteHistory fromAnswer(Answer answer) {
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now());
    }

    private DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
        this.createDate = createDate;
    }

    protected DeleteHistory() {
    }

    public void toDeletedUser(User user) {
        this.deletedByUser = user;
    }

    public Long getContentId() {
        return contentId;
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

    public User getDeletedByUser() {
        return deletedByUser;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedByUser);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deletedByUser.getUserId() +
                ", createDate=" + createDate +
                '}';
    }

    public static List<DeleteHistory> listFromQuestion(Question question) {
        final List<DeleteHistory> deleteHistoryList = new ArrayList<>();
        deleteHistoryList.add(fromQuestion(question));
        for (Answer answer : question.getAnswers()) {
            deleteHistoryList.add(fromAnswer(answer));
        }
        return deleteHistoryList;
    }
}


