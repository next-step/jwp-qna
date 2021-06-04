package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "content_type")
	private ContentType contentType;

	@Column(name = "content_id")
	private Long contentId;

	@ManyToOne
	@JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
	private User deletedBy;

	@Column(name = "create_date")
	private LocalDateTime createDate = LocalDateTime.now();

	protected DeleteHistory() {
	}

	public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deletedBy = deletedBy;
		this.createDate = createDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DeleteHistory that = (DeleteHistory)o;
		return Objects.equals(id, that.id) && contentType == that.contentType && Objects.equals(
			contentId, that.contentId) && Objects.equals(deletedBy, that.deletedBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contentType, contentId, deletedBy);
	}

	public Long getId() {
		return id;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public Long getContentId() {
		return contentId;
	}

	public User getDeletedBy() {
		return deletedBy;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void toDeletedBy(User user) {
		if (this.deletedBy != null) {
			user.getDeleteHistories().remove(this);
		}
		this.deletedBy = user;
		user.getDeleteHistories().add(this);
	}
}
