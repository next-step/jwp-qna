package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "content_id")
	private Long contentId;

	@Column(name = "content_type")
	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	@Column(name = "create_date")
	private LocalDateTime createDate = LocalDateTime.now();

	@Column(name = "deleted_by_id")
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

	public void setId(Long id) {
		this.id = id;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
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
			", createDate=" + createDate +
			'}';
	}
}
