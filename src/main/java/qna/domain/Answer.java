package qna.domain;

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
import javax.persistence.OneToOne;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Lob
	private String contents;

	@Column(nullable = false)
	private boolean deleted = false;

	@ManyToOne
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private Question question;

	@OneToOne
	@JoinColumn(name = "wirter_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
	private User writer;

	protected Answer() {
	}

	public Answer(User writer, Question question, String contents) {
		this(null, writer, question, contents);
	}

	public Answer(Long id, User writer, Question question, String contents) {
		this.id = id;

		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException();
		}

		if (Objects.isNull(question)) {
			throw new NotFoundException();
		}

		this.writer = writer;
		this.question = question;
		this.contents = contents;
	}

	public void toQuestion(Question question) {
		this.question = question;
	}

	public Long id() {
		return id;
	}

	public User writer() {
		return writer;
	}

	public void writtenBy(User writer) {
		this.writer = writer;
	}

	public Question question() {
		return question;
	}

	public String contents() {
		return contents;
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete() {
		this.deleted = true;
	}

	@Override
	public String toString() {
		return "Answer{"
			+ "id=" + id
			+ ", writer=" + writer
			+ ", question=" + question
			+ ", contents='" + contents + '\''
			+ ", deleted=" + deleted
			+ '}';
	}

	@Override
	public boolean equals(Object obejct) {
		if (this == obejct) {
			return true;
		}
		if (!(obejct instanceof Answer)) {
			return false;
		}
		Answer answer = (Answer) obejct;
		return deleted == answer.deleted
			&& Objects.equals(id, answer.id)
			&& Objects.equals(contents, answer.contents)
			&& Objects.equals(question, answer.question)
			&& Objects.equals(writer, answer.writer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contents, deleted, question, writer);
	}
}
