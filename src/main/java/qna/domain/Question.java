package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table (name = "T_question")
public class Question extends BaseEntity {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;

	@Column (length = 100, nullable = false)
	private String title;

	@Lob
	private String contents;

	@OneToOne (fetch = LAZY, cascade = CascadeType.MERGE)
	@JoinColumn (name = "user_id")
	private User user;

	@OneToMany(mappedBy = "question", fetch = LAZY)
	private List<Answer> answers = new ArrayList<> ();

	@Column (nullable = false)
	private boolean deleted = false;

	protected Question () {
	}

	public Question (String title, String contents) {
		this(null, title, contents);
	}

	public Question (Long id, String title, String contents) {
		this.id = id;
		this.title = title;
		this.contents = contents;
	}

	public Question writeBy (User user) {
		this.user = user;
		return this;
	}

	public boolean isOwner (User user) {
		return this.user.equals(user);
	}

	public void addAnswer (Answer answer) {
		answer.toQuestion(this);
		this.answers.add (answer);
	}

	public Long id () {
		return id;
	}

	public void id (Long id) {
		this.id = id;
	}

	public String title () {
		return title;
	}

	public void title (String title) {
		this.title = title;
	}

	public String contents () {
		return contents;
	}

	public void contents (String contents) {
		this.contents = contents;
	}

	public User writer () {
		return user;
	}

	public void writer (User user) {
		this.user = user;

	}

	public boolean deleted () {
		return deleted;
	}

	public void deleted (boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString () {
		return "Question{" +
				"id=" + id +
				", title='" + title + '\'' +
				", contents='" + contents + '\'' +
				", writerId=" + user.id() +
				", deleted=" + deleted +
				'}';
	}

	public List<DeleteHistory> deleteQuestion(User loginUser) throws CannotDeleteException {
		if (!this.isOwner(loginUser)) {
			throw new CannotDeleteException ("질문을 삭제할 권한이 없습니다.");
		}

		for (Answer answer: this.answers) {
			if(!answer.isOwner (loginUser)) {
				throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
			}
		}

		List<DeleteHistory> deleteHistories = new ArrayList<> ();

		this.deleted(true);
		deleteHistories.add (new DeleteHistory (ContentType.QUESTION, this.id, this.writer (), LocalDateTime.now ()));

		for (Answer answer: this.answers) {
			answer.deleted (true);
			deleteHistories.add (new DeleteHistory (ContentType.ANSWER, answer.id(), answer.writer (), LocalDateTime.now ()));
		}
		return deleteHistories;
	}
}
