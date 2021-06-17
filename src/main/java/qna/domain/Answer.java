package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
@Table(name = "answer")
public class Answer {
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

	@ManyToOne
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private Question question;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
	private User user;

	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "delete_history_id")
	private List<DeleteHistory> deleteHistories = new ArrayList<>();

	protected Answer() {
	}

	public Answer(User writer, Question question, String contents) {
		this(null, writer, question, contents);
	}

	public Answer(Long id, User writer, Question question, String contents) {
		this.id = id;

		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException();
		}

		if (Objects.isNull(question)) {
			throw new NotFoundException();
		}

		this.user = writer;
		this.question = question;
		this.contents = contents;
	}

	public void toQuestion(Question question) {
		this.question = question;
	}

	public Long getId() {
		return id;
	}

	public User getWriter() {
		return this.user;
	}

	public boolean isOwner(User writer) {
		return this.user.equals(writer);
	}

	public boolean isAnsweredQuestion(Question question) {
		return this.question.equals(question);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete(User loginUser) {
		delete();
		addDeleteHistory();
	}

	private void addDeleteHistory() {
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		deleteHistories.add(new DeleteHistory(ContentType.ANSWER, id, user, LocalDateTime.now()));
		this.deleteHistories.addAll(deleteHistories);
	}

	public void delete() {
		this.deleted = true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Answer answer = (Answer)o;
		return deleted == answer.deleted &&
			Objects.equals(id, answer.id) &&
			Objects.equals(contents, answer.contents);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contents, createAt, deleted, question, updatedAt, user);
	}

	@Override
	public String toString() {
		return "Answer{" +
			"id=" + id +
			", contents='" + contents + '\'' +
			", createAt=" + createAt +
			", deleted=" + deleted +
			", question=" + question +
			", updatedAt=" + updatedAt +
			", user=" + user +
			'}';
	}

	public List<DeleteHistory> getDeleteHistories() {
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		deleteHistories.addAll(this.deleteHistories);
		return deleteHistories;
	}
}
