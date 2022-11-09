package qna.domain.answer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.domain.deletehistory.DeleteHistory;
import qna.domain.user.User;

@Embeddable
public class Answers {
	@OneToMany(mappedBy = "question")
	private final List<Answer> answers;

	public Answers() {
		this.answers = new ArrayList<>();
	}

	public void add(final Answer answer) {
		answers.add(answer);
	}

	public boolean isEmpty() {
		return answers.isEmpty();
	}

	public List<DeleteHistory> deleteAll(User loginUser) {
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
