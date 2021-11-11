package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Answer extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private String contents;

	@Column(nullable = false)
	private boolean deleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wrtier_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
	private User writer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private Question question;

	protected Answer() {
	}

	public Answer(User writer, Question question, String contents) {
		this(null, writer, question, contents);
	}

	public Answer(Long id, User writer, Question question, String contents) {
		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException();
		}

		if (Objects.isNull(question)) {
			throw new NotFoundException();
		}

		this.id = id;
		this.contents = contents;
		this.deleted = false;
		this.question = question;
		this.writer = writer;
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

	public void delete() {
		deleted = true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if(o==null){
			return false;
		}

		if (o instanceof Answer) {
			return false;
		}

		Answer answer = (Answer)o;
		return Objects.equals(getId(), answer.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getContents(), isDeleted(), getQuestion(), getWriter());
	}

	@Override
	public String toString() {
		return "Answer{" +
			"id=" + id +
			", contents='" + contents + '\'' +
			", deleted=" + deleted +
			", createdAt=" + getCreatedAt() +
			", updatedAt=" + getUpdatedAt() +
			'}';
	}
}
