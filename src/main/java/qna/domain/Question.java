package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import qna.CannotDeleteException;
import qna.ForbiddenException;

@Entity
@Table(name = "question")
public class Question extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String title;

	@Lob
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
	private User writer;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private List<Answer> answers = new ArrayList<>();

	@Column(nullable = false)
	private boolean deleted = false;

	public Question() {
	}

	public Question(String title, String contents) {
		this(null, title, contents);
	}

	public Question(Long id, String title, String contents) {
		this.id = id;
		this.title = title;
		this.contents = contents;
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
		if(answers.contains(answer)){
			throw new ForbiddenException();
		}
		answers.add(answer);
	}

	public Long getId() {
		return id;
	}

	public User getWriter() {
		return writer;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
		if (!this.canDeleteQuestion(loginUser)) {
			throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
		}
		setDelete();

		List<DeleteHistory> deleteHistories = addDeleteHistories(loginUser);
		return deleteHistories;
	}

	private List<DeleteHistory> addDeleteHistories(User loginUser) {
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		deleteHistories.add(new DeleteHistory(ContentType.QUESTION, this.getId(), loginUser));

		this.getAnswers().stream()
			.map(answer -> new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser))
			.forEach(deleteHistory -> deleteHistories.add(deleteHistory));
		return deleteHistories;
	}

	public boolean isAnswersByUser(User loginUser){
		return this.answers.stream()
			.allMatch(answer -> answer.isOwner(loginUser));
	}

	public boolean canDeleteQuestion(User loginUser){
		return isOwner(loginUser) && isAnswersByUser(loginUser);
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}

	@Override
	public String toString() {
		return "Question{" +
			"id=" + id +
			", title='" + title + '\'' +
			", contents='" + contents + '\'' +
			", writer=" + writer +
			", deleted=" + deleted +
			'}';
	}

	public void setDelete() {
		this.deleted = true;
		this.answers.forEach(answer -> answer.setDeleted(true));
	}
}
