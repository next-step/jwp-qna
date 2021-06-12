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

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Answer> answers = new ArrayList<>();

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

	public String getTitle() {
		return title;
	}

	public String getContents() {
		return contents;
	}

	public Long getWriterId() {
		return user.getId();
	}

	public User getWriter() {
		return this.user;
	}

	public void setWriter(final User user) {
		this.user = user;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isContaioned(Answer answer) {
		return answers.contains(answer);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Question question = (Question)o;
		return deleted == question.deleted &&
			Objects.equals(id, question.id);
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
