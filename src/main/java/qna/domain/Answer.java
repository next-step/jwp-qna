package qna.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.vo.Contents;
import qna.domain.vo.Deleted;

@Entity
public class Answer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Contents contents;

	@Embedded
	private Deleted deleted;

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
		validateWriterIsNotNull(writer);
		validateQuestionIsNotNull(question);
		this.id = id;
		this.writer = writer;
		this.contents = Contents.generate(contents);
		this.deleted = Deleted.generate();
		changeQuestion(question);
	}

	private void validateQuestionIsNotNull(Question question) {
		if (Objects.isNull(question)) {
			throw new NotFoundException();
		}
	}

	private void validateWriterIsNotNull(User writer) {
		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException();
		}
	}

	public void changeQuestion(Question question) {
		if (!Objects.isNull(this.question)) {
			this.question.removeAnswer(this);
		}
		question(question);
		question.addAnswer(this);
		updatedAtNow();
	}


	public Long id() {
		return id;
	}

	public User writer() {
		return writer;
	}

	public void writtenBy(User writer) {
		this.writer = writer;
		updatedAtNow();
	}

	public Question question() {
		return question;
	}

	public void question(Question question) {
		this.question = question;
		updatedAtNow();
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void validateIsOwner(User loginUser) throws CannotDeleteException {
		if (!isOwner(loginUser)) {
			throw new CannotDeleteException("사용자와 답변의 작성자가 일치하지 않습니다.");
		}
	}

	public boolean isDeleted() {
		return deleted.value();
	}

	public void delete() {
		deleted.changeDeleted();
		updatedAtNow();
	}

	@Override
	public String toString() {
		return "Answer{"
			+ "id=" + id
			+ ", writer=" + writer
			+ ", contents='" + contents + '\''
			+ ", deleted=" + deleted
			+ '}';
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Answer)) {
			return false;
		}
		Answer answer = (Answer) object;
		return isAllEquals(answer);
	}

	private boolean isAllEquals(Answer answer) {
		return Objects.equals(id, answer.id)
			&& Objects.equals(contents, answer.contents)
			&& Objects.equals(deleted, answer.deleted)
			&& Objects.equals(question, answer.question)
			&& Objects.equals(writer, answer.writer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contents, deleted, question, writer);
	}
}
