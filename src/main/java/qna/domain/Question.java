package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Question extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	private String contents;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(nullable = false)
	private boolean deleted;

	private Long writerId;

	protected Question() {
	}

	public Question(String title, String contents) {
		this(null, title, contents);
	}

	public Question(Long id, String title, String contents) {
		this.id = id;
		this.contents = contents;
		this.title = title;
		this.deleted = false;
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
		deleted = true;
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
		return isDeleted() == question.isDeleted() && Objects.equals(getId(), question.getId())
			&& Objects.equals(getContents(), question.getContents()) && Objects.equals(getTitle(),
			question.getTitle()) && Objects.equals(getWriterId(), question.getWriterId())
			&& Objects.equals(getCreatedAt(), question.getCreatedAt()) && Objects.equals(getUpdatedAt(),
			question.getUpdatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getContents(), getTitle(), isDeleted(), getWriterId());
	}

	@Override
	public String toString() {
		return "Question{" +
			"id=" + id +
			", title='" + title + '\'' +
			", contents='" + contents + '\'' +
			", writerId=" + writerId +
			", deleted=" + deleted +
			", createdAt=" + getCreatedAt() +
			", updatedAt=" + getUpdatedAt() +
			'}';
	}

}
