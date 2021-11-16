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
	private Answers answers = new Answers();

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

	public Question writeBy(User writer) {
		this.writer = writer;
		writer.getQuestions().add(this);
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

	protected void setWriter(User writer) {
		this.writer = writer;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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

	public List<DeleteHistory> delete(User user) throws CannotDeleteException {
		if (!isOwner(user)) {
			throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
		}

		if (!this.answers.containsAllSameWriter(user)) {
			throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
		}

		this.deleted = true;

		DeleteHistory questionDeleteHistory = new DeleteHistory(
			ContentType.QUESTION, this.id, this.writer, LocalDateTime.now());

		List<DeleteHistory> deleteHistories = new ArrayList<>();
		deleteHistories.add(questionDeleteHistory);
		deleteHistories.addAll(this.answers.deleteAll());
		return deleteHistories;
	}
}
