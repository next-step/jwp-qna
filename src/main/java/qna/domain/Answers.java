package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	private List<Answer> answers = new ArrayList<>();

	public Answers() {
	}

	public Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public void add(Answer answer) {
		answers.add(answer);
	}

	public boolean hasAnswer(Answer answer){
		return answers.contains(answer);
	}

	public void deleteAnswers(User loginUser) throws CannotDeleteException {
		for (Answer answer : answers) {
			answer.delete(loginUser);
		}
	}

	public List<Answer> getAnswers() {
		return answers;
	}
}
