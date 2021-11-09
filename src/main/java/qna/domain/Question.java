package qna.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Question extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long id;
	private String contents;
	private String title;
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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Question question = (Question)o;
		return isDeleted() == question.isDeleted() && Objects.equals(getId(), question.getId())
			&& Objects.equals(getContents(), question.getContents()) && Objects.equals(getTitle(),
			question.getTitle()) && Objects.equals(getWriterId(), question.getWriterId());
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
			'}';
	}
}
