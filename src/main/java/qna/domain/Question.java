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

@Entity
public class Question extends AuditEntity {
	public static final String MESSAGE_NOT_AUTHENTICATED_ON_DELETE = "질문을 삭제할 권한이 없습니다.";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Lob
	private String contents;
	@Column(nullable = false)
	private boolean deleted = false;
	@Column(length = 100, nullable = false)
	private String title;
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;
	@Embedded
	private Answers answers = new Answers();

	public Question(String title, String contents) {
		this(null, title, contents);
	}

	protected Question() {
	}

	public Question(Long id, String title, String contents) {
		this.id = id;
		this.title = title;
		this.contents = contents;
	}

	public Question writeBy(User writer) {
		this.writer = writer;
		return this;
	}

	public boolean isOwner(User writer) {
		return this.writer.getId().equals(writer.getId());
	}

	public void addAnswer(Answer answer) {
		answers.add(answer);
		if (!answer.getQuestion().equals(this)) {
			answer.toQuestion(this);
		}
	}

	public Long getId() {
		return id;
	}

	public Answers getAnswers() {
		return answers;
	}

	public User getWriter() {
		return writer;
	}

	public boolean isDeleted() {
		return deleted;
	}

	private void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public DeleteHistories delete(User loginUser) throws CannotDeleteException {
		if (!isOwner(loginUser)) {
			throw new CannotDeleteException(MESSAGE_NOT_AUTHENTICATED_ON_DELETE);
		}
		this.setDeleted(true);
		DeleteHistories deleteHistories = answers.delete(loginUser);
		deleteHistories.add(this);
		return deleteHistories;
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
		return Objects.hash(id);
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
}
