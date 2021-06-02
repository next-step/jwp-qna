package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question")
	List<Answer> answers = new ArrayList<>();

	public void add(Answer answer) {
		this.answers.add(answer);
	}

	public void remove(Answer answer) {
		this.answers.remove(answer);
	}

	public int getSize() {
		return this.answers.size();
	}

	public List<DeleteHistory> deleteAll(User loginUser) throws CannotDeleteException {
		List<DeleteHistory> answerDeleteHistories = new ArrayList<>();
		for (Answer answer : this.answers) {
			answerDeleteHistories.add(answer.delete(loginUser));
		}
		return answerDeleteHistories;
	}

}
