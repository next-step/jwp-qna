package qna.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import qna.CannotDeleteException;

@Entity
public class Question extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Lob
	private String contents;

	@Column(nullable = false)
	private Boolean deleted = Boolean.FALSE;

	@Column(nullable = false, length = 100)
	private String title;

	@ManyToOne
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

	@Embedded
	private AnswerGroup answerGroup = AnswerGroup.generate();

	protected Question() {
	}

	public Question(String title, String contents) {
		this(null, title, contents);
	}

	public Question(Long id, String title, String contents) {
		this.id = id;
		this.title = title;
		this.contents = contents;
	}

	public Long id() {
		return id;
	}

	public String title() {
		return title;
	}

	public String contents() {
		return contents;
	}

	public User writer() {
		return writer;
	}

	public List<Answer> answers() {
		return answerGroup.answers();
	}

	public Question writeBy(User writer) {
		this.writer = writer;
		updatedAtNow();
		return this;
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void addAnswer(Answer answer) {
		if (!answers().contains(answer)) {
			answers().add(answer);
		}
		answer.question(this);
		updatedAtNow();
	}

	public void removeAnswer(Answer answer) {
		answers().remove(answer);
		updatedAtNow();
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete(User loginUser) throws CannotDeleteException {
		validateCouldDelete(loginUser);
		this.deleted = true;
		answerGroup.deleteAll();
		updatedAtNow();
	}

	public void validateCouldDelete(User loginUser) throws CannotDeleteException {
		validateIsSameWithUserAndWriter(loginUser);
		validateAnswerGroup(loginUser);
	}

	private void validateAnswerGroup(User loginUser) throws CannotDeleteException {
		if (answerGroup.isEmpty()) {
			return;
		}
		answerGroup.validateIsSameWithUserAndWriter(loginUser);
	}

	private void validateIsSameWithUserAndWriter(User loginUser) throws CannotDeleteException {
		if (!isOwner(loginUser)) {
			throw new CannotDeleteException("질문 삭제는 작성자만 가능합니다.");
		}
	}

	public DeleteHistory generateDeleteHistoryOfQuestion() {
		return new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now());
	}

	public List<DeleteHistory> generateDeleteHistoryAllOfAnswers() {
		return answerGroup.generateDeleteHistoryAllOfAnswers();
	}

	@Override
	public String toString() {
		return "Question{" +
			"id=" + id +
			", contents='" + contents + '\'' +
			", deleted=" + deleted +
			", title='" + title + '\'' +
			", writer=" + writer +
			'}';
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Question)) {
			return false;
		}
		Question question = (Question) object;
		return deleted == question.deleted
			&& Objects.equals(id, question.id)
			&& Objects.equals(contents, question.contents)
			&& Objects.equals(title, question.title)
			&& writer.equals(question.writer)
			&& answerGroup.containsAll(question.answerGroup)
			&& question.answerGroup.containsAll(answerGroup);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, contents, deleted, title, writer, answerGroup);
	}
}
