package qna.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import qna.CannotDeleteException;

@Entity
public class Question extends BaseTimeEntity {

	@Column(name = "title", length = 100, nullable = false)
	private String title;

	@Lob
	@Column(name = "contents")
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;

	@Embedded
	private Answers answers = new Answers();

	protected Question() {
	}

	public Question(String title, String contents) {
		this(null, title, contents);
	}

	public Question(Long id, String title, String contents) {
		this.setId(id);
		this.title = title;
		this.contents = contents;
	}

	public Question writeBy(User writer) {
		this.writer = writer;
		return this;
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void addAnswer(Answer answer) {
		answers.add(answer);
		answer.toQuestion(this);
	}

	public void addAnswers(List<Answer> answerList) {
		answers.addAll(answerList);
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

	public Answers getAnswers() {
		return answers;
	}

	public void validDeleteUser(User user) throws CannotDeleteException {
		if (!isOwner(user)) {
			throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
		}
	}

	public DeleteHistories delete(User user) throws CannotDeleteException {
		DeleteHistories deleteHistories = getAnswers().deleteAll(user);
		setDeleted(true);
		deleteHistories.add(new DeleteHistory(ContentType.QUESTION, getId(), writer));

		return deleteHistories;
	}

	@Override
	public String toString() {
		return "Question{" +
			"id=" + getId() +
			", title='" + title + '\'' +
			", contents='" + contents + '\'' +
			", writer=" + writer +
			", deleted=" + deleted +
			'}';
	}
}