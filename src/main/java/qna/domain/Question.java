package qna.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String title;

	@Lob
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

	@OneToMany(mappedBy = "question")
	private List<Answer> answers = Collections.emptyList();

	@Column(nullable = false)
	private boolean deleted = false;

	public Question(String title, String contents) {
		this.title = title;
		this.contents = contents;
	}

	protected Question() {
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

	public List<Answer> getAnswers() {
		return answers;
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
		this.deleted = true;
	}

	@Override
	public String toString() {
		return "Question{" +
				"id=" + id +
				", title='" + title + '\'' +
				", contents='" + contents + '\'' +
				", writerId=" + writer +
				", deleted=" + deleted +
				'}';
	}
}
