package qna.domain;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

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
import javax.persistence.Table;

import qna.ErrorMessage;
import qna.UnAuthorizedException;

@Entity
@Table(name = "question")
public class Question extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100)
	private String title;

	@Lob
	private String contents;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

	@OneToMany(mappedBy = "question", cascade = ALL)
	private List<Answer> answers = new ArrayList();

	@Column(nullable = false)
	private boolean deleted = false;

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

	public Question writeBy(User writer) {
		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException(ErrorMessage.USER_IS_NOT_NULL);
		}
		this.writer = writer;
		return this;
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void addAnswer(Answer answer) {
		answer.toQuestion(this);
		answers.add(answer);
	}

	public List<Answer> getAnswers() {
		return answers;
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

	public String getContents() {
		return contents;
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
		return Objects.equals(id, question.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Question{" +
			"id=" + id +
			", title='" + title + '\'' +
			", contents='" + contents + '\'' +
			", writer=" + writer +
			", deleted=" + deleted +
			'}';
	}
}
