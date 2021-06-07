package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wirter_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
	private User writer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private Question question;

	@Lob
	private String contents;

	@Column(nullable = false)
	private boolean deleted = false;

	protected Answer() {
	}

	public Answer(String contents) {
		this.contents = contents;
	}

	public Answer(User writer, Question question, String contents) {
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
		return writer.equals(this.writer);
	}

	public void toQuestion(Question question) {
		this.question = question;
		question.addAnswerData(this);
	}

	public void writeBy(User writer) {
		this.writer = writer;
	}

	public Long getId() {
		return id;
	}

	public User getWriter() {
		return writer;
	}

	public Question getQuestion() {
		return question;
	}

	public String getContents() {
		return contents;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public DeleteHistory delete() {
		this.deleted = true;
		return new DeleteHistory(ContentType.ANSWER, id, writer);
	}

	@Override
	public String toString() {
		return "Answer{" +
				"id=" + id +
				", writerId=" + writer.getId() +
				", questionId=" + question.getId() +
				", contents='" + contents + '\'' +
				", deleted=" + deleted +
				'}';
	}
}
