package qna.domain.question;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import qna.domain.User;
import qna.domain.exception.question.AnswerOwnerNotMatchedException;

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
	 */
	public List<Answer> deleteAllBy(User deleteBy) throws AnswerOwnerNotMatchedException {
		List<Answer> deleted = new ArrayList<>();
		for (Answer answer : answers) {
			answer.deleteBy(deleteBy);
			deleted.add(answer);
		}
		answers.removeAll(deleted);
		return deleted;
	}
}
