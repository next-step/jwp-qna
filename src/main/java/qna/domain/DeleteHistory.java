package qna.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	private Long contentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
	private User deletedBy;

	public DeleteHistory(ContentType contentType, Long contentId, User deletedBy) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deletedBy = deletedBy;
	}

	protected DeleteHistory() {
	}

	public Long getId() {
		return id;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public User getDeletedBy() {
		return deletedBy;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DeleteHistory that = (DeleteHistory) o;
		return Objects.equals(id, that.id) &&
				contentType == that.contentType &&
				Objects.equals(contentId, that.contentId) &&
				Objects.equals(deletedBy, that.deletedBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contentType, contentId, deletedBy);
	}

	@Override
	public String toString() {
		return "DeleteHistory{" +
				"id=" + id +
				", contentType=" + contentType +
				", contentId=" + contentId +
				", deletedById=" + deletedBy +
				", createAt=" + getCreatedAt() +
				'}';
	}
}
