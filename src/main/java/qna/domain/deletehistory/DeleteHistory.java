package qna.domain.deletehistory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import qna.domain.ContentType;
import qna.domain.common.BaseEntity;
import qna.domain.question.Question;
import qna.domain.user.User;

@Entity
public class DeleteHistory extends BaseEntity {

	protected DeleteHistory() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	private Long contentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"), nullable = false)
	private User deletedBy;

	private LocalDateTime createDate = LocalDateTime.now();

	public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
		this.contentType = contentType;
		this.contentId = contentId;
		this.deletedBy = deletedBy;
		this.createDate = createDate;
	}

	public static DeleteHistory questionDeleteHistory(Question question) {
		return new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now());
	}

	private static DeleteHistory answerDeleteHistory(Question question) {
		return new DeleteHistory(ContentType.ANSWER, question.getId(), question.getWriter(), LocalDateTime.now());
	}

	public static List<DeleteHistory> questionDeleteHistories(Question question) {
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		if (!question.getAnswers().isEmpty()) {
			deleteHistories.addAll(answerDeleteHistories(question));
		}
		DeleteHistory questionDeleteHistory = questionDeleteHistory(question);
		deleteHistories.add(questionDeleteHistory);
		return deleteHistories;
	}

	private static List<DeleteHistory> answerDeleteHistories(Question question) {
		return question.getAnswers().stream()
			.map(answer -> answerDeleteHistory(question))
			.collect(Collectors.toList());
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		DeleteHistory that = (DeleteHistory)o;

		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "DeleteHistory{" +
			"id=" + id +
			", contentType=" + contentType +
			", contentId=" + contentId +
			", deletedById=" + deletedBy +
			", createDate=" + createDate +
			'}';
	}
}
