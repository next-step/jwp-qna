package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {
	private final List<DeleteHistory> deleteHistories = new ArrayList<>();

	public void add(Question question) {
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(),
			question.getWriter());
		deleteHistories.add(deleteHistory);
	}

	public void add(Answers answers) {
		for (Answer answer : answers.getAnswers()) {
			DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, answer.getId(),
				answer.getWriter());
			deleteHistories.add(deleteHistory);
		}
	}

	public List<DeleteHistory> get() {
		return deleteHistories;
	}
}
