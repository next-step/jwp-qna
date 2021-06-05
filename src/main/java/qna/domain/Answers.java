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

	protected Answers() {
	}

	public List<DeleteHistory> deleteAnswers(User loginUser) throws CannotDeleteException {
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		for (Answer answer : this.answers) {
			deleteHistories.add(answer.delete(loginUser));
		}
		return deleteHistories;
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
