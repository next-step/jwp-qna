package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

@Entity
public class Answer extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@JoinColumn(name = "writer_id")
	private User writer;
	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;
	@Lob
	private String contents;
	private boolean deleted = false;

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
		this.contents = contents;
	}

	public void toQuestion(Question question) {
		this.question = question;
	}

	public DeleteHistory delete(final User loginUser) throws CannotDeleteException {
		if (!writer.equals(loginUser)) {
			throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
		}
		this.deleted = true;
		return new DeleteHistory(ContentType.ANSWER, id, loginUser.getId(), LocalDateTime.now());
	}

	public boolean isDeleted() {
		return deleted;
	}

	public Long getId() {
		return id;
	}

	public Long getWriterId() {
		return writer.getId();
	}
}
