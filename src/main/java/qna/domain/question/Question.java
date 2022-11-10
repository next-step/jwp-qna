package qna.domain.question;

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
import qna.domain.answer.Answer;
import qna.domain.answer.Answers;
import qna.domain.common.BaseEntity;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.user.User;

@Entity
public class Question extends BaseEntity {

	private static final String QUESTION_DELETE_UNAUTHORIZED_EXCEPTION_MESSAGE = "질문을 삭제할 권한이 없습니다.";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String title;

	@Lob
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"), nullable = false)
	private User writer;

	@Column(nullable = false)
	private boolean deleted = false;

	@Embedded
	private final Answers answers = new Answers();

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
		answer.toQuestion(this);
		this.answers.add(answer);
	}

	public Answers getAnswers() {
		return this.answers;
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

	public List<DeleteHistory> delete(User loginUser) {
		validateAuthentication(loginUser);
		List<DeleteHistory> deleteHistories = deleteHistories(loginUser);
		delete();
		return deleteHistories;
	}

	private List<DeleteHistory> deleteHistories(User loginUser) {
		List<DeleteHistory> deleteHistories = deleteHistoriesOfAnswers(loginUser);
		deleteHistories.add(DeleteHistory.ofQuestion(this));
		return deleteHistories;
	}

	private List<DeleteHistory> deleteHistoriesOfAnswers(User loginUser) {
		if (answers.isEmpty()) {
			return new ArrayList<>();
		}
		return answers.deleteAll(loginUser);
	}

	private void delete() {
		this.deleted = true;
	}

	private void validateAuthentication(User loginUser) {
		if (!isOwner(loginUser)) {
			throw new CannotDeleteException(QUESTION_DELETE_UNAUTHORIZED_EXCEPTION_MESSAGE);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Question question = (Question)o;

		return Objects.equals(id, question.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "Question{" +
			"id=" + id +
			", title='" + title + '\'' +
			", contents='" + contents + '\'' +
			", writerId=" + writer +
			", deleted=" + deleted +
			'}';
	}
}
