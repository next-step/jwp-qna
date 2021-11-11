package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DeleteHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long contentId;

	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	private LocalDateTime createDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
	private User deleter;

	protected DeleteHistory() {
	}

	public DeleteHistory(ContentType contentType, Long contentId, User deleter, LocalDateTime createDate) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deleter = deleter;
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

	public User getDeleter() {
		return deleter;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null) {
			return false;
		}

		if (!(o instanceof DeleteHistory)) {
			return false;
		}

		DeleteHistory that = (DeleteHistory)o;

		return Objects.equals(getId(), that.getId()) && Objects.equals(getContentId(),	that.getContentId())
			&& getContentType() == that.getContentType();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getContentId(), getContentType());
	}

	@Override
	public String toString() {
		return "DeleteHistory{" +
			"id=" + id +
			", contentId=" + contentId +
			", contentType=" + contentType +
			", createDate=" + createDate +
			'}';
	}
}
