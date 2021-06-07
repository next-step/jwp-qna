package qna.domain.question;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.function.Function;

/**
 *
 * @author heetaek.kim
 */
public final class AnswerList {

	private List<Answer> answers;

	public AnswerList(List<Answer> answers) {
		this.answers = answers;
	}

	public <T> List<T> mapToDeleteHistoryList(Function<Answer, T> mapper) {
		return answers.stream()
			.map(mapper)
			.collect(toList());
	}
}
