package qna.domain.answer;

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

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.common.BaseEntity;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.question.Question;
import qna.domain.user.User;

@Entity
public class Answer extends BaseEntity {

	protected Answer() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wirter_Id", foreignKey = @ForeignKey(name = "fk_answer_writer"), nullable = false)
	private User writer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"), nullable = false)
	private Question question;

	@Lob
	private String contents;

	@Column(nullable = false)
	private boolean deleted = false;

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

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public DeleteHistory delete(User loginUser) {
		if (!isOwner(loginUser)) {
			throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
		}
		this.deleted = true;
		return DeleteHistory.ofAnswer(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Answer answer = (Answer)o;

		return Objects.equals(id, answer.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Answer{" +
			"id=" + id +
			", writerId=" + writer +
			", questionId=" + question +
			", contents='" + contents + '\'' +
			", deleted=" + deleted +
			'}';
	}

}
