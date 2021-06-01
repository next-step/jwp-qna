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
public class DeleteHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "content_type")
	private ContentType contentType;

	@Column(name = "content_id")
	private Long contentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delete_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
	private User deletedByUser;

	@Column(name = "create_date")
	private LocalDateTime createDate;

	protected DeleteHistory() {

	}

	public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deletedByUser = deletedByUser;
		this.createDate = createDate;
	}

	public Long getId() {
		return id;
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
			Objects.equals(deletedByUser, that.deletedByUser);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contentType, contentId, deletedByUser);
	}

	@Override
	public String toString() {
		return "DeleteHistory{" +
			"id=" + id +
			", contentType=" + contentType +
			", contentId=" + contentId +
			", deletedByUser=" + deletedByUser +
			", createDate=" + createDate +
			'}';
	}
}
