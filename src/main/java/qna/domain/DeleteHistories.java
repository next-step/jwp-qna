package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DeleteHistories that = (DeleteHistories)o;
		return Objects.equals(deleteHistories, that.deleteHistories);
	}

	@Override
	public int hashCode() {
		return Objects.hash(deleteHistories);
	}
}
