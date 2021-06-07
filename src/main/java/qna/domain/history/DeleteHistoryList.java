package qna.domain.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import qna.domain.DeleteHistory;
import qna.domain.question.Answer;
import qna.domain.question.AnswerList;
import qna.domain.question.Question;

/**
 *
 * @author heetaek.kim
 */
public class DeleteHistoryList {

	private final List<DeleteHistory> histories = new ArrayList<>();

	public void addQuestionHistory(Question question, AnswerList deletedAnswers) {
		histories.add(new DeleteHistory(question));
		histories.addAll(deletedAnswers.mapToDeleteHistoryList(this::answerToHistory));
	}

	private DeleteHistory answerToHistory(Answer answer) {
		return new DeleteHistory(answer);
	}

	public List<DeleteHistory> toList() {
		return Collections.unmodifiableList(histories);
	}
}
