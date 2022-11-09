package qna.domain.answer;

import java.util.ArrayList;
import java.util.List;

import qna.CannotDeleteException;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.question.Question;
import qna.domain.user.User;

public class Answers {
	private List<Answer> answers;

	private Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public static Answers of(Question question) {
		return new Answers(question.getAnswers());
	}

	public List<DeleteHistory> deleteAll(User loginUser) throws CannotDeleteException {
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		for (Answer answer : answers) {
			deleteHistories.add(answer.delete(loginUser));
		}
		return deleteHistories;
	}

	public boolean allMatchLoginUser(User loginUser) {
		return answers.stream()
			.allMatch(it -> loginUser.equals(it.getWriter()));
	}
}
