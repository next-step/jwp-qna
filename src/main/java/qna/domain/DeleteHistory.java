package qna.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table
@Getter
@Setter
public class DeleteHistory extends BaseEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    @Column
    private Long contentId;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    public DeleteHistory(ContentType contentType, Long contentId, User deleteUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.user = deleteUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, user);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + user +
                ", createDate=" + createdAt +
                '}';
    }

    public void setUser(User deleteUser) {
        this.user = deleteUser;
        this.user.addDeleteHistory(this);
    }
}
