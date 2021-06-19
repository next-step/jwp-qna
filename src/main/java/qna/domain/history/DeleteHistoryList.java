package qna.domain.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import qna.domain.DeleteHistory;
import qna.domain.exception.QuestionNotDeletedException;
import qna.domain.question.Answer;
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
		histories.addAll(question.answers().mapToDeleteHistoryList(this::answerToHistory));
	}

	private DeleteHistory answerToHistory(Answer answer) {
		return new DeleteHistory(answer);
	}

	public List<DeleteHistory> toList() {
		return Collections.unmodifiableList(histories);
	}
}
