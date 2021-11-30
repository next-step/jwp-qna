package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import qna.CannotDeleteException;

@Entity
public class Question extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String title;

	@Lob
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	private User writer;

	@Embedded
	private Answers answers = new Answers();

	private boolean deleted = false;

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

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void addAnswer(Answer answer) {
		answers.add(answer);
		if (answer.getQuestion() != this) {
			answer.toQuestion(this);
		}
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

	public Answers getAnswers() {
		return answers;
	}

	public DeleteHistories delete(User loginUser) throws CannotDeleteException {
		if (!this.isOwner(loginUser)) {
			throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
		}

		List<DeleteHistory> deleteHistoryList = new ArrayList<>();

		final DeleteHistories deleteHistories = this.answers.delete(loginUser);

		this.setDeleted(true);
		deleteHistoryList.add(
			DeleteHistory.ofQuestion(id, this.getWriter()));

		deleteHistoryList.addAll(deleteHistories.getDeleteHistories());

		return new DeleteHistories(deleteHistoryList);
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
			", writer=" + writer +
			", deleted=" + deleted +
			'}';
	}
}
