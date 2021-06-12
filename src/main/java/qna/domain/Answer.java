package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
@Table(name = "answer")
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "contents")
	private String contents;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createAt = LocalDateTime.now();

	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;

	@ManyToOne
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private Question question;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
	private User user;

	protected Answer() {
	}

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

		this.user = writer;
		this.question = question;
		this.contents = contents;
	}

	public void toQuestion(Question question) {
		this.question = question;
	}

	public Long getId() {
		return id;
	}

	public User getWriter() {
		return this.user;
	}

	public boolean isOwner(User writer) {
		return this.user.equals(writer);
	}

	public boolean isAnsweredQuestion(Question question) {
		return this.question.equals(question);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Answer answer = (Answer)o;
		return deleted == answer.deleted &&
			Objects.equals(id, answer.id) &&
			Objects.equals(contents, answer.contents) &&
			Objects.equals(createAt, answer.createAt) &&
			Objects.equals(question, answer.question) &&
			Objects.equals(updatedAt, answer.updatedAt) &&
			Objects.equals(user, answer.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contents, createAt, deleted, question, updatedAt, user);
	}
}
