package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long writerId;

	private Long questionId;

	@Lob
	private String contents;

	@Column(nullable = false)
	private boolean deleted = false;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	private LocalDateTime updatedAt;

	protected Answer() {
	}

	public Answer(User writer, Question question, String contents) {
		if (Objects.isNull(writer)) {
			throw new UnAuthorizedException();
		}

		if (Objects.isNull(question)) {
			throw new NotFoundException();
		}

		this.writerId = writer.getId();
		this.questionId = question.getId();
		this.contents = contents;
	}

	public boolean isOwner(User writer) {
		return writer.getId().equals(this.writerId);
	}

	public void toQuestion(Question question) {
		this.questionId = question.getId();
	}

	public Long getId() {
		return id;
	}

	public Long getWriterId() {
		return writerId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public String getContents() {
		return contents;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void delete() {
		this.deleted = true;
	}

	@Override
	public String toString() {
		return "Answer{" +
				"id=" + id +
				", writerId=" + writerId +
				", questionId=" + questionId +
				", contents='" + contents + '\'' +
				", deleted=" + deleted +
				'}';
	}
}
