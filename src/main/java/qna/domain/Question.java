package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import qna.CannotDeleteException;

@Entity
public class Question extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	@Column
	private String contents;

	@Column(nullable = false)
	private boolean deleted = false;

	@Column(length = 100, nullable = false)
	private String title;

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
		this.title = title;
		this.contents = contents;
	}

	public Question(Long id, String title, String contents, boolean deleted) {
		this.id = id;
		this.title = title;
		this.contents = contents;
		this.deleted = deleted;
	}

	public List<DeleteHistory> delete(User user) throws CannotDeleteException {
		validateDelete(user);
		this.deleted = true;
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		deleteHistories.add(createDeleteHistory());
		deleteHistories.addAll(this.answers.deleteAll());
		return deleteHistories;
	}

	private DeleteHistory createDeleteHistory() {
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
		return deleted;
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
