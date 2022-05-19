package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import qna.domain.common.DatedAtEntity;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

@Entity
@Table(name = "answer")
public class Answer extends DatedAtEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "contents")
	private String contents;
	@NotNull
	@Column(name = "deleted")
	private boolean deleted = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
	private User writer;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private Question question;

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
		this.question = question;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getWriter() {
		return writer;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestionId(Question question) {
		this.question = question;
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

	@Override
	public String toString() {
		return "Answer{" + "id=" + id + ", writerId=" + writer.getId() + ", questionId=" + question.getId()
				+ ", contents='" + contents + '\'' + ", deleted=" + deleted + '}';
	}
}
