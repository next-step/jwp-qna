package qna.domain.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import qna.domain.DeleteHistory;
import qna.domain.exception.question.AnswerNotDeletedException;
import qna.domain.exception.question.QuestionNotDeletedException;
import qna.domain.question.Answer;
import qna.domain.question.AnswerList;
import qna.domain.question.Question;

/**
 *
 * @author heetaek.kim
 */
public class DeleteHistoryList {

	private final List<DeleteHistory> histories = new ArrayList<>();

	public DeleteHistoryList(Question question) throws QuestionNotDeletedException {
		if (!question.isDeleted()) {
			throw new QuestionNotDeletedException(question);
		}
		histories.add(new DeleteHistory(question));
	}

	public DeleteHistoryList(AnswerList answerList) throws AnswerNotDeletedException {
		if (answerList.hasUndeleted()) {
			throw new AnswerNotDeletedException(answerList);
		}
		histories.addAll(answerList.mapToDeleteHistoryList(this::answerToHistory));
	}

	/**
	 * merge list.
	 */
	public DeleteHistoryList(DeleteHistoryList... lists) {
		Arrays.stream(lists)
			.map(list -> list.histories)
			.flatMap(List::stream)
			.forEach(histories::add);
	}

	private DeleteHistory answerToHistory(Answer answer) {
		return new DeleteHistory(answer);
	}

	public List<DeleteHistory> toList() {
		return Collections.unmodifiableList(histories);
	}
}
