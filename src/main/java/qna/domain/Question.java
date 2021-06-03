package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "question")
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String title;

	@Lob
	private String contents;

	private Long writerId;

	@Column(nullable = false)
	private boolean deleted = false;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	public Question(String title, String contents) {
		this.title = title;
		this.contents = contents;
	}

	protected Question() {
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

	public String getTitle() {
		return title;
	}

	public String getContents() {
		return contents;
	}

	public Long getWriterId() {
		return writerId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete() {
		this.deleted = true;
	}

	@Override
	public String toString() {
		return "Question{" +
				"id=" + id +
				", title='" + title + '\'' +
				", contents='" + contents + '\'' +
				", writerId=" + writerId +
				", deleted=" + deleted +
				'}';
	}
}
