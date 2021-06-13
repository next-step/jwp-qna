package qna.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
import qna.domain.code.ContentType;
import qna.domain.vo.ContentId;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private ContentId contentId;

	@Column(name = "content_type")
	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	@Column(name = "created_date")
	private LocalDateTime createDate = LocalDateTime.now();

	@ManyToOne
	@JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
	private User deleter;

	protected DeleteHistory() {
	}

	private DeleteHistory(ContentType contentType, Long contentId, User deleter) {
		this.contentType = contentType;
		this.contentId = ContentId.generate(contentId);
		this.deleter = deleter;
		this.createDate = LocalDateTime.now();
	}

	public static DeleteHistory ofQuestion(Long contentId, User deleter) {
		return new DeleteHistory(ContentType.QUESTION, contentId, deleter);
	}

	public static DeleteHistory ofAnswer(Long contentId, User deleter) {
		return new DeleteHistory(ContentType.ANSWER, contentId, deleter);
	}

	public ContentType contentType() {
		return contentType;
	}

	public Long id() {
		return id;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof DeleteHistory)) {
			return false;
		}
		DeleteHistory that = (DeleteHistory) object;
		return Objects.equals(id, that.id)
			&& contentId.equals(that.contentId)
			&& contentType == that.contentType
			&& deleter.equals(that.deleter);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contentId, contentType, deleter);
	}

	@Override
	public String toString() {
		return "DeleteHistory{"
			+ "id=" + id
			+ ", contentType=" + contentType
			+ ", contentId=" + contentId
			+ ", deleter=" + deleter
			+ ", createDate=" + createDate
			+ '}';
	}
}
