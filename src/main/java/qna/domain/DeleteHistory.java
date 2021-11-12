package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DeleteHistory extends BaseTimeEntity {

	@Enumerated(EnumType.STRING)
	@Column(name = "contentType")
	private ContentType contentType;

	@Column(name = "content_id")
	private Long contentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
	private User deletedByUser;

	protected DeleteHistory() {
	}

	public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deletedByUser = deletedByUser;
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

	public User getDeletedByUser() {
		return deletedByUser;
	}

	public void setDeletedByUser(User deletedByUser) {
		this.deletedByUser = deletedByUser;
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
			Objects.equals(deletedByUser, that.deletedByUser);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), contentType, contentId, deletedByUser);
	}

	@Override
	public String toString() {
		return "DeleteHistory{" +
			"id=" + getId() +
			", contentType=" + contentType +
			", contentId=" + contentId +
			", deletedByUser=" + deletedByUser +
			'}';
	}
}
