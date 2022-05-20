package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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

    @ManyToOne
    @JoinColumn(name = "deleted_by_id")
    private User user;

    @Column
    @CreatedDate
    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    public DeleteHistory(DeleteHistoryContent deleteHistoryContent, User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
        this.deleteHistoryContent = deleteHistoryContent;
        this.user = writer;
    }

    public Long getId() {
        return id;
    }

    public DeleteHistoryContent contentInformation() {
        return this.deleteHistoryContent;
    }

    public User getDeleter() {
        return user;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", deleteHistoryContent=" + deleteHistoryContent +
                ", user=" + user +
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
        return Objects.equals(id, that.id) && Objects.equals(deleteHistoryContent,
                that.deleteHistoryContent) && Objects.equals(user, that.user) && Objects.equals(
                createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deleteHistoryContent, user, createDate);
    }
}
