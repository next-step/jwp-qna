package qna.domain;

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
import qna.ErrorMessage;

@Entity
public class Question extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String title;

	@Lob
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

	@Column(nullable = false)
	private boolean deleted = false;

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
		return this;
	}

	private boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void addAnswer(Answer answer) {
		answer.toQuestion(this);
		answers.add(answer);
	}

	public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
		if (!isOwner(loginUser)) {
			throw new CannotDeleteException(ErrorMessage.CAN_NOT_DELETE_QUESTION_WITHOUT_OWNERSHIP.getContent());
		}
		final List<DeleteHistory> deletedAnswerHistories = deleteAnswers(loginUser);
		setDeleted(true);
		return new ArrayList<DeleteHistory>() {{
			add(new DeleteHistory(ContentType.QUESTION, getId(), getWriter()));
			addAll(deletedAnswerHistories);
		}};
	}

	private List<DeleteHistory> deleteAnswers(User loginUser) throws CannotDeleteException {
		try {
			return answers.delete(loginUser);
		} catch (CannotDeleteException e) {
			throw new CannotDeleteException(
				ErrorMessage.CAN_NOT_DELETE_QUESTION_HAVING_ANSWER_WRITTEN_BY_OTHER.getContent());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public User getWriter() {
		return writer;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Question))
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

	@Override
	public String toString() {
		return "Question{" +
			"id=" + id +
			", title='" + title + '\'' +
			", contents='" + contents + '\'' +
			", writer=" + writer +
			", deleted=" + deleted +
			", answers.size=" + answers.size() +
			'}';
	}
}
