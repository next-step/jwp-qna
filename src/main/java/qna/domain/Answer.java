package qna.domain;

import static javax.persistence.FetchType.*;
import static qna.ErrorMessage.*;

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

import qna.QuestionNotFoundException;
import qna.UnAuthorizedException;

@Entity
public class Answer extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	@Column
	private String contents;

	@Column(nullable = false)
	private boolean deleted = false;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private Question question;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
	private User user;

	protected Answer() {
	}

	public Answer(User writer, Question question, String contents) {
		this(null, writer, question, contents);
	}

	public Answer(Long id, User writer, Question question, String contents) {
		this.id = id;

		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException(USER_IS_NOT_NULL);
		}

		if (Objects.isNull(question)) {
			throw new QuestionNotFoundException(QUESTION_NOT_FOUND);
		}

		this.user = writer;
		this.question = question;
		this.contents = contents;
	}

	public boolean isOwner(User writer) {
		return this.user.equals(writer);
	}

	public void toQuestion(Question question) {
		this.question = question;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public Question getQuestion() {
		return question;
	}

	public User getUser() {
		return user;
	}

	public String getContents() {
		return contents;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Answer{" +
			"id=" + id +
			", contents='" + contents + '\'' +
			", deleted=" + deleted +
			", question=" + question +
			", user=" + user +
			'}';
	}
}
