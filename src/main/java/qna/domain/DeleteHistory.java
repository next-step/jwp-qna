package qna.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long contentId;
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    private LocalDateTime createDate = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "DELETED_BY_ID")
    private User user;

    protected DeleteHistory() {}

    public DeleteHistory(ContentType contentType, Long contentId, User user, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.createDate = createDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, user.getId());
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + user.getId() +
                ", createDate=" + createDate +
                '}';
    }
}
