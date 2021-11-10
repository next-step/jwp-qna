package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Answer extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	private String contents;

	@Column(nullable = false)
	private boolean deleted;

	private Long questionId;

	private Long writerId;

	protected Answer() {
	}

	public Answer(User writer, Question question, String contents) {
		this(null, writer, question, contents);
	}

	public Answer(Long id, User writer, Question question, String contents) {
		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException();
		}

		if (Objects.isNull(question)) {
			throw new NotFoundException();
		}

		this.id = id;
		this.contents = contents;
		this.deleted = false;
		this.writerId = writer.getId();
		this.questionId = question.getId();
	}

	public boolean isOwner(User writer) {
		return this.writerId.equals(writer.getId());
	}

	public void toQuestion(Question question) {
		this.questionId = question.getId();
	}

	public Long getId() {
		return id;
	}

	public Long getWriterId() {
		return writerId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public String getContents() {
		return contents;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete() {
		deleted = true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Answer answer = (Answer)o;
		return isDeleted() == answer.isDeleted() && Objects.equals(getId(), answer.getId())
			&& Objects.equals(getContents(), answer.getContents()) && Objects.equals(getQuestionId(),
			answer.getQuestionId()) && Objects.equals(getWriterId(), answer.getWriterId())
			&& Objects.equals(getCreatedAt(), answer.getCreatedAt()) && Objects.equals(getUpdatedAt(),
			answer.getUpdatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getContents(), isDeleted(), getQuestionId(), getWriterId());
	}

	@Override
	public String toString() {
		return "Answer{" +
			"id=" + id +
			", contents='" + contents + '\'' +
			", deleted=" + deleted +
			", questionId=" + questionId +
			", writerId=" + writerId +
			", createdAt=" + getCreatedAt() +
			", updatedAt=" + getUpdatedAt() +
			'}';
	}
}
