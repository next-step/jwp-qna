package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
@Where(clause = "deleted = 'false'")
public class Answer extends BaseEntity {

	@Lob
	@Column(name = "contents")
	private String contents;

	@ColumnDefault("false")
	@Column(name = "deleted", nullable = false)
	private boolean deleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	@Where(clause = "question.deleted = 'false'")
	private Question question;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
	private User writer;

	protected Answer() {
		super();
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

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void toQuestion(Question question) {
		if (Objects.nonNull(this.question)) {
			this.question.getAnswers().remove(this);
		}
		this.question = question;
		question.getAnswers().add(this);
	}

	public User getWriter() {
		return this.writer;
	}

	public Question getQuestion() {
		return this.question;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public DeleteHistory delete(User loginUser) throws CannotDeleteException {
		validateDelete(loginUser);
		this.setDeleted(true);
		return new DeleteHistory(ContentType.ANSWER, this.id, this.writer, LocalDateTime.now());
	}

	private void validateDelete(User loginUser) throws CannotDeleteException {
		if (!this.isOwner(loginUser)) {
			throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
		}
	}
}
