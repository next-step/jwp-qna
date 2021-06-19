package qna.domain.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import qna.domain.DeleteHistory;
import qna.domain.exception.question.AnswerNotDeletedException;
import qna.domain.exception.question.QuestionNotDeletedException;
import qna.domain.question.Answer;
import qna.domain.question.Answers;
import qna.domain.question.Question;

/**
 *
 * @author heetaek.kim
 */
public class DeleteHistories {

	private final Collection<DeleteHistory> histories = new ArrayList<>();

	public DeleteHistories(Question question) throws QuestionNotDeletedException {
		if (!question.isDeleted()) {
			throw new QuestionNotDeletedException(question);
		}
		histories.add(new DeleteHistory(question));
	}

	public DeleteHistories(Answers answers) throws AnswerNotDeletedException {
		if (answers.hasUndeleted()) {
			throw new AnswerNotDeletedException(answers);
		}
		histories.addAll(answers.mapToDeleteHistories(this::answerToHistory));
	}

	/**
	 * merge list.
	 */
	public DeleteHistories(DeleteHistories... lists) {
		Arrays.stream(lists)
			.map(list -> list.histories)
			.flatMap(Collection::stream)
			.forEach(histories::add);
	}

	private DeleteHistory answerToHistory(Answer answer) {
		return new DeleteHistory(answer);
	}

	public Collection<DeleteHistory> toCollection() {
		return Collections.unmodifiableCollection(histories);
	}
}
