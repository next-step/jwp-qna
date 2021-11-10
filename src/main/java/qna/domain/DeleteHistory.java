package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class DeleteHistory extends BaseTimeEntity {

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

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getDeletedById() {
		return deletedById;
	}

	public void setDeletedById(Long deletedById) {
		this.deletedById = deletedById;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DeleteHistory that = (DeleteHistory)o;
		return Objects.equals(getId(), that.getId()) &&
			contentType == that.contentType &&
			Objects.equals(contentId, that.contentId) &&
			Objects.equals(deletedById, that.deletedById);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), contentType, contentId, deletedById);
	}

	@Override
	public String toString() {
		return "DeleteHistory{" +
			"id=" + getId() +
			", contentType=" + contentType +
			", contentId=" + contentId +
			", deletedById=" + deletedById +
			'}';
	}
}
