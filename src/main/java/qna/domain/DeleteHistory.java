package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.data.annotation.CreatedDate;
import qna.exception.UnAuthorizedException;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private DeleteHistoryContent deleteHistoryContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id")
    private User deleter;

    @Column
    @CreatedDate
    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    public DeleteHistory(DeleteHistoryContent deleteHistoryContent, User deleter) {
        nullCheckContentAndUser(deleteHistoryContent, deleter);
        this.deleteHistoryContent = deleteHistoryContent;
        this.deleter = deleter;
    }

    public static DeleteHistory delete(Question question) {
        return new DeleteHistory(DeleteHistoryContent.remove(question), question.getWriter());
    }

    public static DeleteHistory delete(Answer answer) {
        return new DeleteHistory(DeleteHistoryContent.remove(answer), answer.getWriter());
    }

    private void nullCheckContentAndUser(DeleteHistoryContent deleteHistoryContent, User deleter) {
        if (Objects.isNull(deleteHistoryContent)) {
            throw new IllegalArgumentException("삭제 콘텐츠 정보가 존재하지 않습니다.");
        }

        if (Objects.isNull(deleter)) {
            throw new UnAuthorizedException("유저 정보가 존재하지 않습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public DeleteHistoryContent contentInformation() {
        return this.deleteHistoryContent;
    }

    public User getDeleter() {
        return deleter;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", deleteHistoryContent=" + deleteHistoryContent +
                ", deleter=" + deleter +
                ", createDate=" + createDate +
                '}';
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
        return Objects.equals(id, that.id) && Objects.equals(deleteHistoryContent, that.deleteHistoryContent)
                && Objects.equals(deleter.getId(), that.deleter.getId()) && Objects.equals(createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deleteHistoryContent, deleter.getId(), createDate);
    }
}
