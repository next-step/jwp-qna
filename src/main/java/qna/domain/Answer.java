package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "writer_id", nullable = false)
	private Long writerId;
	@Column(name = "question_id", nullable = false)
	private Long questionId;
	@Lob
	@Column(name = "contents")
	private String contents;
	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;

	protected Answer() {
	}

	public Answer(User writer, Question question, String contents) {

		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException();
		}

		if (Objects.isNull(question)) {
			throw new NotFoundException();
		}

		this.writerId = writer.getId();
		this.questionId = question.getId();
		this.contents = contents;
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

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Answer{" +
			"id=" + id +
			", writerId=" + writerId +
			", questionId=" + questionId +
			", contents='" + contents + '\'' +
			", deleted=" + deleted +
			'}';
	}
}
