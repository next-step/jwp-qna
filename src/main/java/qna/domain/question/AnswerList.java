package qna.domain.question;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import qna.domain.User;
import qna.domain.exception.question.AnswerNotDeletedException;
import qna.domain.exception.question.AnswerOwnerNotMatchedException;
import qna.domain.history.DeleteHistoryList;

/**
 *
 * @author heetaek.kim
 */
public final class AnswerList {

	private final List<Answer> answers;

	public AnswerList(List<Answer> answers) {
		this.answers = answers;
	}

	public <T> List<T> mapToDeleteHistoryList(Function<Answer, T> mapper) {
		return answers.stream()
			.map(mapper)
			.collect(toList());
	}

	/**
	 * @throws AnswerOwnerNotMatchedException 삭제하는 사용자가 권한이 없을 경우 발생.
	 * @return Deleted answer list.
	 */
	public DeleteHistoryList deleteAllBy(User deleteBy) throws
			AnswerOwnerNotMatchedException {
		List<Answer> deleted = new ArrayList<>();
		for (Answer answer : answers) {
			answer.deleteBy(deleteBy);
			deleted.add(answer);
		}
		try {
			return new DeleteHistoryList(new AnswerList(deleted));
		} catch (AnswerNotDeletedException e) {
			// 발생하지 않으므로 RuntimeException 적용.
			throw new RuntimeException(e);
		}
	}

	public boolean hasUndeleted() {
		return this.answers.stream()
			.anyMatch(answer -> !answer.isDeleted());
	}
}
