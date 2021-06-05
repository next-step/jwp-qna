package qna.domain;

import java.time.LocalDateTime;
import java.util.List;
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
public class Question extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String title;

	@Lob
	@Column(name = "contents")
	private String contents;

	@ManyToOne
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;

	@Embedded
	private Answers answers = new Answers();

	protected Question() {
	}

	public Question(String title, String contents) {
		this.title = title;
		this.contents = contents;
	}

	public Question writeBy(User writer) {
		this.writer = writer;
		return this;
	}

	public void addAnswerInQuestion(Answer answer) {
		this.answers.addAnswer(answer);
	}

	public void removeAnswerInQuestion(Answer answer) {
		this.answers.removeAnswer(answer);
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void addAnswer(Answer answer) {
		answer.toQuestion(this);
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContents() {
		return contents;
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

	public void toWriter(User user) {
		if (this.writer != null) {
			user.getQuestions().remove(this);
		}
		this.writer = user;
		user.getQuestions().add(this);
	}

	public List<DeleteHistory> delete(User user) throws CannotDeleteException {
		if (!this.isOwner(user)) {
			throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
		}

		List<DeleteHistory> deleteHistories = this.answers.deleteAnswers(user);
		this.setDeleted(true);

		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, this.id, this.writer,
			LocalDateTime.now());

		deleteHistories.add(deleteHistory);
		return deleteHistories;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Question question = (Question)o;
		return deleted == question.deleted && Objects.equals(id, question.id) && Objects.equals(title,
			question.title) && Objects.equals(contents, question.contents) && Objects.equals(writer,
			question.writer) && Objects.equals(answers, question.answers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, contents, writer, deleted, answers);
	}

	public boolean isAnswerContains(Answer answer) {
		return this.answers.isContains(answer);
	}

	@Override
	public String toString() {
		return "Question{" +
			"id=" + id +
			", title='" + title + '\'' +
			", contents='" + contents + '\'' +
			", writerID=" + writer.getId() +
			", deleted=" + deleted +
			", answers=" + answers +
			'}';
	}
}
