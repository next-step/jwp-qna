package qna.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

@Entity
@Table(name = "question")
public class Question {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@Column(name = "title", length = 100)
	private String title;
	@Column(name = "contents")
	private String contents;
	@Column(name = "writer_id")
	private Long writerId;
	@NotNull
	@Column(name = "deleted")
	private boolean deleted = false;
	@NotNull
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public Question(String title, String contents) {
		this(null, title, contents);
	}

	public Question(Long id, String title, String contents) {
		this.id = id;
		this.title = title;
		this.contents = contents;
	}

	public Question writeBy(User writer) {
		this.writerId = writer.getId();
		return this;
	}

	public boolean isOwner(User writer) {
		return this.writerId.equals(writer.getId());
	}

	public void addAnswer(Answer answer) {
		answer.toQuestion(this);
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

	public Long getWriterId() {
		return writerId;
	}

	public void setWriterId(Long writerId) {
		this.writerId = writerId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Question{" + "id=" + id + ", title='" + title + '\'' + ", contents='" + contents + '\'' + ", writerId="
				+ writerId + ", deleted=" + deleted + '}';
	}
}
