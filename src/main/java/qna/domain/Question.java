package qna.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import qna.CannotDeleteException;

@Entity
public class Question extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Embedded
	private Contents contents;

	@Column(nullable = false)
	@Embedded
	private Deleted deleted = Deleted.ofFalse();

	@Embedded
	@Column(length = Title.MAX_LENGTH, nullable = false)
	private Title title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

	@Embedded
	private final Answers answers = Answers.of();

	protected Question() {
	}

	public Question(String title, String contents) {
		this(null, title, contents);
	}

	public Question(Long id, String title, String contents) {
		this.id = id;
		this.title = Title.of(title);
		this.contents = new Contents(contents);
	}

	public Question(Long id, String title, String contents, boolean deleted) {
		this.id = id;
		this.title = Title.of(title);
		this.contents = new Contents(contents);
		this.deleted = Deleted.of(deleted);
	}

	public List<DeleteHistory> delete(User user) throws CannotDeleteException {
		validateDelete(user);
		this.deleted = Deleted.ofTure();
		return DeleteHistories.of(createDeleteHistory())
			.combine(this.answers.deleteAll())
			.toList();
	}

	public DeleteHistory createDeleteHistory() {
		return new DeleteHistory(
			ContentType.QUESTION, this.id, this.writer, LocalDateTime.now());
	}

	private void validateDelete(User user) throws CannotDeleteException {
		if (!isOwner(user)) {
			throw new CannotDeleteException(ErrorCode.DELETE_QUESTION_FORBIDDEN.getMessage());
		}

		if (!this.answers.isAllSameWriter(user)) {
			throw new CannotDeleteException(ErrorCode.DELETE_QUESTION_OTHER_WRITER_ANSWER.getMessage());
		}
	}

	public Question writeBy(User writer) {
		this.writer = writer;
		return this;
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void addAnswer(Answer answer) {
		answer.toQuestion(this);
		this.answers.add(answer);
	}

	public Question addAnswers(List<Answer> answers) {
		this.answers.addAll(answers);
		return this;
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

	@Override
	public String toString() {
		return "Question{" +
			"id=" + id +
			", title='" + title + '\'' +
			", contents='" + contents + '\'' +
			", writerId=" + writer.getId() +
			", deleted=" + deleted +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Question question = (Question)o;

		return Objects.equals(id, question.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}
