package qna.domain;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private Question question;

	@ManyToOne
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fw_answer_writer"))
	private User writer;

	@Lob
	@Column(name = "contents")
	private String contents;

	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;

	protected Answer() {
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

	public Long getId() {
		return id;
	}

	public boolean isOwner(User writer) {
		return Optional.ofNullable(writer)
			.map(user -> user.getId().equals(this.writer.getId()))
			.orElse(false);
	}

	public void toQuestion(Question question) {
		this.question = question;
	}

	public Question getQuestion() {
		return question;
	}

	public User getWriter() {
		return writer;
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

	public void toWriter(User user) {
		this.writer = user;
	}
}
