package qna.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Question extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private String contents;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(nullable = false)
	private boolean deleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id")
	private User writer;

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	private List<Answer> answers;

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
		this.writer = writer;
		return this;
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
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

	public User getWriter() {
		return writer;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete() {
		deleted = true;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if(o==null){
			return false;
		}

		if (o instanceof Question) {
			return false;
		}

		Question question = (Question)o;
		return Objects.equals(getId(), question.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getContents(), getTitle(), isDeleted(), getWriter());
	}

	@Override
	public String toString() {
		return "Question{" +
			"id=" + id +
			", title='" + title + '\'' +
			", contents='" + contents + '\'' +
			", deleted=" + deleted +
			", createdAt=" + getCreatedAt() +
			", updatedAt=" + getUpdatedAt() +
			'}';
	}

}
