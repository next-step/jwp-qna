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

	private Long deletedById;

	public DeleteHistory(ContentType contentType, Long contentId, Long deletedById) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deletedById = deletedById;
	}

	protected DeleteHistory() {
	}

	public Long getId() {
		return id;
	}

	public ContentType getContentType() {
		return contentType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DeleteHistory that = (DeleteHistory) o;
		return Objects.equals(id, that.id) &&
				contentType == that.contentType &&
				Objects.equals(contentId, that.contentId) &&
				Objects.equals(deletedById, that.deletedById);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contentType, contentId, deletedById);
	}

	@Override
	public String toString() {
		return "DeleteHistory{" +
				"id=" + id +
				", contentType=" + contentType +
				", contentId=" + contentId +
				", deletedById=" + deletedById +
				", createAt=" + getCreatedAt() +
				'}';
	}
}
