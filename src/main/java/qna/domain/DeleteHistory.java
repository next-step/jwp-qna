package qna.domain;

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

@Entity
public class DeleteHistory extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long contentId;

	@Column
	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	@JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
	@ManyToOne(fetch = FetchType.LAZY)
	private User deletedBy;

	protected DeleteHistory() {
	}

	public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deletedBy = deletedBy;
		this.createdAt = createDate;
	}

	public DeleteHistory(Long id, ContentType contentType, Long contentId, User deletedBy) {
		this.id = id;
		this.contentType = contentType;
		this.contentId = contentId;
		this.deletedBy = deletedBy;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DeleteHistory that = (DeleteHistory)o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "DeleteHistory{" +
			"id=" + id +
			", contentType=" + contentType +
			", contentId=" + contentId +
			", deletedById=" + deletedBy.getId() +
			", createDate=" + createdAt +
			'}';
	}
}
