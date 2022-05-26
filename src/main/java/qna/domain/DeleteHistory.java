package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id")
    private User deletedUser;

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(Answer answer, User deletedUser) {
        this(ContentType.ANSWER, answer.getId(), deletedUser, LocalDateTime.now());
    }

    public DeleteHistory(Question question, User deletedUser) {
        this(ContentType.QUESTION, question.getId(), deletedUser, LocalDateTime.now());
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedUser = deletedUser;
        this.createDate = createDate;
    }

    public void deletedUser(User deletedUser) {
        this.deletedUser = deletedUser;
    }

    public User getDeletedUser() {
        return deletedUser;
    }

    public Long getId() {
        return id;
    }

    public Long getContentId() {
        return contentId;
    }

    public Long getDeletedById() {
        return deletedUser.getId();
    }

    public static DeleteHistory newQuestionDeleteHistory(Long contentId, User deletedUser, LocalDateTime createDate) {
        return new DeleteHistory(ContentType.QUESTION, contentId, deletedUser, createDate);
    }

    public static DeleteHistory newAnswerDeleteHistory(Long contentId, User deletedUser, LocalDateTime createDate) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deletedUser, createDate);
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
        return Objects.equals(id, that.id) && contentType == that.contentType && Objects
                .equals(contentId, that.contentId) && Objects.equals(deletedUser, that.deletedUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedUser, createDate);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedUser=" + deletedUser +
                ", createDate=" + createDate +
                '}';
    }
}
