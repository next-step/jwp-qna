package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String title;

	@Lob
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
	private final List<Answer> answers = new ArrayList<>();

	@Column(nullable = false)
	private boolean deleted = false;

	public Question(String title, String contents) {
		this.title = title;
		this.contents = contents;
	}

	protected Question() {
	}

	public Question writeBy(User writer) {
		this.writer = writer;
		return this;
	}

	public boolean isOwner(User writer) {
		return this.writer.equals(writer);
	}

	public void addAnswer(Answer answer) {
		answer.toQuestion(this);
	}

	public void addAnswerData(Answer answer) {
		this.answers.add(answer);
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public User getWriter() {
		return writer;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public List<DeleteHistory> delete(User loggedInUser) throws CannotDeleteException {
		validateWriter(loggedInUser);
		validateAnswersOwner(loggedInUser);
		List<DeleteHistory> deleteHistories = this.answers.stream().map(Answer::delete)
				.collect(Collectors.toList());
		this.deleted = true;
		deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, loggedInUser));
		return deleteHistories;
	}

	private void validateWriter(User loggedInUser) throws CannotDeleteException {
		if (!this.writer.matchUser(loggedInUser)) {
			throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
		}
	}

	private void validateAnswersOwner(User loggedInUser) throws CannotDeleteException {
		for (Answer answer : this.answers) {
			if (!answer.isOwner(loggedInUser)) {
				throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
			}
		}
	}

	@Override
	public String toString() {
		return "Question{" +
				"id=" + id +
				", title='" + title + '\'' +
				", contents='" + contents + '\'' +
				", writerId=" + writer +
				", deleted=" + deleted +
				'}';
	}
}
