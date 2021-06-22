package qna.domain.question;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.domain.User;
import qna.domain.exception.question.AnswerNotDeletedException;
import qna.domain.exception.question.AnswerOwnerNotMatchedException;
import qna.domain.history.DeleteHistories;

/**
 *
 * @author heetaek.kim
 */
@Embeddable
public final class Answers {

	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private final Collection<Answer> answers = new ArrayList<>();

	protected Answers() {}

	public <T> Collection<T> mapToDeleteHistories(Function<Answer, T> mapper) {
		return answers.stream()
			.map(mapper)
			.collect(toList());
	}

	/**
	 * @throws AnswerOwnerNotMatchedException 삭제하는 사용자가 권한이 없을 경우 발생.
	 * @return Deleted answer list.
	 */
	public DeleteHistories deleteAllBy(User deleteBy) throws
			AnswerOwnerNotMatchedException {
		for (Answer answer : answers) {
			answer.deleteBy(deleteBy);
		}
		try {
			return new DeleteHistories(this);
		} catch (AnswerNotDeletedException e) {
			// 발생하지 않으므로 RuntimeException 적용.
			throw new RuntimeException(e);
		}
	}

	public boolean hasUndeleted() {
		return this.answers.stream()
			.anyMatch(answer -> !answer.isDeleted());
	}

	public void add(Answer answer) {
		this.answers.add(answer);
	}
}
