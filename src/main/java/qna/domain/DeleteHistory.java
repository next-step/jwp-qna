package qna.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.sql.Delete;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
        setUser(deleteUser);
        this.createdAt = createDate;
    }

    public static DeleteHistory answer(Long contentId, User loginUser) {
        return new DeleteHistory(ContentType.ANSWER,contentId,loginUser,LocalDateTime.now());
    }

    public static DeleteHistory question(Long contentId, User loginUser) {
        return new DeleteHistory(ContentType.QUESTION,contentId,loginUser,LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return contentType == that.contentType &&
                Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, contentId);
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
