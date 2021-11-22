package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "delete_history")
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id")
    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @CreatedDate
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleteUser;    // User의 mappedBy와 일치해야 함.

    // Arguments가 없는 Default Constructor 생성
    protected DeleteHistory() {}

    // User 객체를 통해 연관관계가 매핑이 되기 때문에 기존 deletedById Parameter를 User 객체로 바꿔 사용
    // 만약 기존 파라미터인 deleteById를 사용할 경우 UserRepository를 통해 User 객체를 꺼낸 후 deleteUser와 매핑하기.
    public DeleteHistory(ContentType contentType, Long contentId, User deletedUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleteUser = deletedUser;
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(this.deleteUser.getId(), that.deleteUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, this.deleteUser.getId());
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + this.deleteUser.getId() +
                ", createDate=" + createDate +
                '}';
    }
}
