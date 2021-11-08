package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question")
	private final List<Answer> answers;

	protected Answers() {
		this.answers = new ArrayList<>();
	}

	private Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public static Answers of(List<Answer> answers) {
		return new Answers(answers);
	}

	public static Answers createEmpty() {
		return new Answers(new ArrayList<>());
	}

	public void deleteAll(User owner) throws CannotDeleteException {
		for (Answer answer : answers) {
			answer.delete(owner);
		}
	}

	public void add(Answer answer) {
		this.answers.add(answer);
	}

	public List<Answer> getValues() {
		return this.answers;
	}
}
