package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Lob
	private String contents;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(nullable = false)
	private boolean deleted = false;

	@Column(nullable = false, length = 100)
	private String title;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

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

	public Long id() {
		return id;
	}

	public String title() {
		return title;
	}

	public String contents() {
		return contents;
	}

	public User writer() {
		return writer;
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
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete() {
		this.deleted = true;
	}

	@Override
	public String toString() {
		return "Question{"
			+ "id=" + id
			+ ", title='" + title + '\''
			+ ", contents='" + contents + '\''
			+ ", writer=" + writer
			+ ", deleted=" + deleted
			+ '}';
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Question)) {
			return false;
		}
		Question question = (Question) object;
		return deleted == question.deleted
			&& Objects.equals(id, question.id)
			&& Objects.equals(contents, question.contents)
			&& Objects.equals(createdAt, question.createdAt)
			&& Objects.equals(title, question.title)
			&& Objects.equals(updatedAt, question.updatedAt)
			&& Objects.equals(writer, question.writer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contents, createdAt, deleted, title, updatedAt, writer);
	}
}
