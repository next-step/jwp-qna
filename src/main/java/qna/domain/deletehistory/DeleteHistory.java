package qna.domain.deletehistory;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
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

import qna.domain.ContentType;
import qna.domain.common.BaseEntity;
import qna.domain.user.User;

@Entity
public class DeleteHistory extends BaseEntity {

	protected DeleteHistory() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;

	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	private Long contentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"), nullable = false)
	private User deletedBy;

	private LocalDateTime createDate = LocalDateTime.now();

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
			", createDate=" + createDate +
			'}';
	}
}
