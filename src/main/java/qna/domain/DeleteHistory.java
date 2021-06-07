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

	@ManyToOne
	@JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
	private User user;

	protected DeleteHistory() {
	}

	public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.user = deletedByUser;
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
		return user.getId();
	}

	public User getDeleter() {
		return user;
	}

	public void setDeletedByUser(User deletedByUser) {
		this.user = deletedByUser;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DeleteHistory that = (DeleteHistory)o;
		return Objects.equals(id, that.id) &&
			Objects.equals(contentId, that.contentId) &&
			contentType == that.contentType &&
			Objects.equals(user, that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contentId, contentType, user);
	}
}
