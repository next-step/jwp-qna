package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DeleteHistory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "contentType")
	private ContentType contentType;

	@Column(name = "content_id")
	private Long contentId;

	@Column(name = "deleted_by_id")
	private Long deletedById;

	protected DeleteHistory() {
	}

	public DeleteHistory(ContentType contentType, Long contentId, Long deletedById) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deletedById = deletedById;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DeleteHistory that = (DeleteHistory)o;
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
			'}';
	}
}
