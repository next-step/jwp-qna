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

	public static Answers createEmpty() {
		return new Answers(new ArrayList<>());
	}

	public static Answers of(List<Answer> answers) {
		return new Answers(answers);
	}

	public List<DeleteHistory> deleteAll(User owner) throws CannotDeleteException {
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		for (Answer answer : answers) {
			deleteHistories.add(answer.delete(owner));
		}

		return deleteHistories;
	}

	public boolean isAllDeleted() {
		return this.answers.stream().allMatch(Answer::isDeleted);
	}

	public void add(Answer answer) {
		this.answers.add(answer);
	}
}
