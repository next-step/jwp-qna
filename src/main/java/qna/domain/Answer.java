package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends AuditEntity {
	public static final String MESSAGE_NOT_AUTHENTICATED_ON_DELETE = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Lob
	private String contents;
	@Column(nullable = false)
	private boolean deleted = false;
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private Question question;
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
	private User writer;

	public Answer(User writer, Question question, String contents) {
		this(null, writer, question, contents);
	}

	public Answer(Long id, User writer, Question question, String contents) {
		this.id = id;

		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException();
		}

		if (Objects.isNull(question)) {
			throw new NotFoundException();
		}

		this.writer = writer;
		this.question = question;
		this.contents = contents;
	}

	protected Answer() {
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void toQuestion(Question question) {
		this.question = question;
		if (!question.getAnswers().get().contains(this)) {
			question.addAnswer(this);
		}
	}

	public Question getQuestion() {
		return question;
	}

	public Long getId() {
		return id;
	}

	public User getWriter() {
		return writer;
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
			", writer=" + writer +
			", question=" + question +
			", contents='" + contents + '\'' +
			", deleted=" + deleted +
			'}';
	}

	public void delete(User loginUser) throws CannotDeleteException {
		if (!isOwner(loginUser)) {
			throw new CannotDeleteException(MESSAGE_NOT_AUTHENTICATED_ON_DELETE);
		}
		setDeleted(true);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Answer answer = (Answer)o;
		return Objects.equals(id, answer.id) && Objects.equals(contents, answer.contents)
			&& Objects.equals(question, answer.question) && Objects.equals(createdAt, answer.createdAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contents, question, createdAt);
	}
}
