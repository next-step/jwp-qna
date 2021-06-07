package qna.domain;

import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.OneToMany;

@Entity
public class Question extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Lob
	private String contents;

	@Column(nullable = false)
	private boolean deleted = false;

	@Column(nullable = false, length = 100)
	private String title;

	@ManyToOne
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

	@OneToMany(mappedBy = "question")
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

	public List<Answer> answers() {
		return this.answers;
	}

	public Question writeBy(User writer) {
		this.writer = writer;
		return this;
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void addAnswer(Answer answer) {
		answers.add(answer);
		answer.question(this);
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
			", contents='" + contents + '\'' +
			", deleted=" + deleted +
			", title='" + title + '\'' +
			", writer=" + writer +
			", answers=" + answers +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Question)) {
			return false;
		}
		Question question = (Question) o;
		return deleted == question.deleted
			&& Objects.equals(id, question.id)
			&& Objects.equals(contents, question.contents)
			&& Objects.equals(title, question.title)
			&& writer.equals(question.writer)
			&& answers.containsAll(question.answers)
			&& question.answers.containsAll(answers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contents, deleted, title, writer, answers);
	}
}
