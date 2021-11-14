package qna.domain;

import java.time.LocalDateTime;
import java.util.Dictionary;
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

	/* 임의로 deleted를 체크하지 않은  DeleteHistory를 생성하는 것을 방지하고자 private 접근제어자로 변경	*/
	private DeleteHistory(ContentType contentType, Long contentId, User deleter) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deleter = deleter;
		this.createDate = LocalDateTime.now();
	}

	/*
	 * DeleteHistory 생성시 객체 그대로 전달하여 클라이언트(DeleteHistory 참조하는 부분)는
	 * DeleteHistory 생성자 로직을 어떻게 가져가는지는 신경쓰지 않도록 정적 팩토리 메서드를 제공
	 */
	public static DeleteHistory makeDeletedHistory(Question question, User deleter) {
		validateDeleted(question.isDeleted());
		return new DeleteHistory(ContentType.QUESTION, question.getId(), deleter);
	}

	public static DeleteHistory makeDeletedHistory(Answer answer, User deleter) {
		validateDeleted(answer.isDeleted());
		return new DeleteHistory(ContentType.ANSWER, answer.getId(), deleter);
	}

	private static void validateDeleted(boolean deleted) {
		if (!deleted) {
			throw new IllegalStateException("삭제되지 않은 컨텐츠입니다.");
		}
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

		return Objects.equals(getId(), that.getId()) && Objects.equals(getContentId(), that.getContentId())
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
