package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DeleteHistory {

	@Id
	@GeneratedValue
	private Long id;

	private Long contentId;

	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	private LocalDateTime createDate;

	private Long deletedById;

	protected DeleteHistory() {
	}

	public DeleteHistory(ContentType contentType, Long contentId, Long deletedById, LocalDateTime createDate) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deletedById = deletedById;
		this.createDate = createDate;
	}

	public Long getId() {
		return id;
	}

	public Long getContentId() {
		return contentId;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public Long getDeletedById() {
		return deletedById;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		DeleteHistory that = (DeleteHistory)o;
		return Objects.equals(id, that.id) && contentType == that.contentType &&
			Objects.equals(contentId, that.contentId) && Objects.equals(deletedById, that.deletedById);
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
			", createDate=" + createDate +
			'}';
	}

}
