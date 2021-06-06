package qna.domain;

import java.time.LocalDateTime;

import javax.persistence.Basic;
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
		setWriter(writer);

		return this;
	}

	public boolean isOwner(User writer) {
		return this.user.equals(writer);
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

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
