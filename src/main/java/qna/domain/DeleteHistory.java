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

	@Column(name = "created_date")
	private LocalDateTime createDate = LocalDateTime.now();

	@Column(name = "deleted_by_id")
	private Long deletedById;

	public DeleteHistory() {
	}

	public DeleteHistory(ContentType contentType, Long contentId, Long deletedById, LocalDateTime createDate) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deletedById = deletedById;
		this.createDate = createDate;
	}

	public Long getId() {
		return this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		DeleteHistory that = (DeleteHistory)obj;
		return Objects.equals(id, that.id)
			&& contentType == that.contentType
			&& Objects.equals(contentId, that.contentId)
			&& Objects.equals(deletedById, that.deletedById);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contentType, contentId, deletedById);
	}

	@Override
	public String toString() {
		return "DeleteHistory{"
			+ "id=" + id
			+ ", contentType=" + contentType
			+ ", contentId=" + contentId
			+ ", deletedById=" + deletedById
			+ ", createDate=" + createDate
			+ '}';
	}
}
