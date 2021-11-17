package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Contents contents;

	@Column(nullable = false)
	private Deleted deleted = Deleted.ofFalse();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private Question question;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
	private User writer;

	protected Answer() {
	}

	public Answer(User writer, Question question, String contents) {
		this(null, writer, question, contents);
	}

	public Answer(Long id, User writer, Question question, String contents) {
		this.id = id;
		validateInit(writer, question);
		this.writer = writer;
		this.question = question;
		this.contents = new Contents(contents);
	}

	private void validateInit(User writer, Question question) {
		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException();
		}

		if (Objects.isNull(question)) {
			throw new NotFoundException();
		}
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void toQuestion(Question question) {
		this.question = question;
	}

	public Long getId() {
		return id;
	}

	public User getWriter() {
		return writer;
	}

	public boolean isDeleted() {
		return deleted.toBoolean();
	}

	public boolean isSameContents(String contents) {
		return Objects.equals(this.contents, new Contents(contents));
	}

	@Override
	public String toString() {
		return "Answer{" +
			"id=" + id +
			", writerId=" + writer.getId() +
			", questionId=" + question.getId() +
			", contents='" + contents + '\'' +
			", deleted=" + deleted +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Answer answer = (Answer)o;

		return Objects.equals(id, answer.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	public DeleteHistory delete() {
		this.deleted = Deleted.ofTure();
		return new DeleteHistory(ContentType.ANSWER, this.id, this.writer, LocalDateTime.now());
	}

	public Answer updateContents(String updateContents) {
		this.contents = new Contents(updateContents);
		return this;
	}
}
