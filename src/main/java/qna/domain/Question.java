package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
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
import javax.persistence.Table;

import qna.CannotDeleteException;

@Entity
@Table(name = "question")
public class Question {
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

	@Column(name = "title", nullable = false, length = 100)
	private String title;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();

	@ManyToOne
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User user;

	@Embedded
	private Answers answers = new Answers();

	// @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
	// @JoinColumn(name = "delete_history_id")
	// private List<DeleteHistory> deleteHistories = new ArrayList<>();
	@Embedded
	private DeleteHistories deleteHistories = new DeleteHistories();

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

	public Question writtenBy(User writer) {
		this.user = writer;

		return this;
	}

	public boolean isOwner(User writer) {
		return this.user.equals(writer);
	}

	public void addAnswer(Answer answer) {
		answer.toQuestion(this);
		answers.add(answer);
	}

	public Long getId() {
		return id;
	}

	public User getWriter() {
		return this.user;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete() {
		this.deleted = true;
	}

	public boolean isContained(Answer answer) {
		return answers.contains(answer);
	}

	public void delete(User loginUser) throws CannotDeleteException {
		validateQuestionWriter(loginUser);
		validateAnswersWriter(loginUser);
		delete();
		answers.delete(loginUser);
		addDeleteHistory(loginUser);
	}

	public List<DeleteHistory> getDeleteHistories() {
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		deleteHistories.addAll(this.deleteHistories.getList());
		deleteHistories.addAll(answers.getDeleteHistories());
		return deleteHistories;
	}

	private void addDeleteHistory(User loginUser) {
		this.deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, loginUser, LocalDateTime.now()));
	}

	private void validateAnswersWriter(User loginUser) throws CannotDeleteException {
		answers.validateAnswersWriter(loginUser);
	}

	private void validateQuestionWriter(User loginUser) throws CannotDeleteException {
		if (!this.user.equals(loginUser)) {
			throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Question question = (Question)o;
		return deleted == question.deleted &&
			Objects.equals(id, question.id) &&
			Objects.equals(contents, question.contents) &&
			Objects.equals(title, question.title) &&
			Objects.equals(user, question.user) &&
			Objects.equals(answers, question.answers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contents, createAt, deleted, title, updatedAt, user, answers);
	}

	@Override
	public String toString() {
		return "Question{" +
			"id=" + id +
			", contents='" + contents + '\'' +
			", createAt=" + createAt +
			", deleted=" + deleted +
			", title='" + title + '\'' +
			", updatedAt=" + updatedAt +
			", user=" + user +
			'}';
	}
}
