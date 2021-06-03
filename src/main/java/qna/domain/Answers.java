package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question")
	private final List<Answer> answers = new ArrayList<>();

	public void deleteAnswers(User loginUser) throws CannotDeleteException {
		for (Answer answer : this.answers) {
			answer.delete(loginUser);
		}
	}

	public void removeAnswer(Answer answer) {
		this.answers.remove(answer);
	}

	public void addAnswer(Answer answer) {
		this.answers.add(answer);
	}

	public boolean isContains(Answer answer) {
		return this.answers.contains(answer);
	}
}
