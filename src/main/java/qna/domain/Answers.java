package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question")
	private final List<Answer> answers = new ArrayList<>();

	protected Answers() {
	}

	public DeleteHistories delete(User loginUser) throws CannotDeleteException {
		List<DeleteHistory> deleteHistories = new ArrayList<>();

		for (Answer answer : getAnswersNotDeleted()) {
			deleteHistories.add(answer.delete(loginUser));
		}

		return new DeleteHistories(deleteHistories);
	}

	public void add(Answer answer) {
		this.answers.add(answer);
	}

	private List<Answer> getAnswersNotDeleted() {
		final List<Answer> answersNotDeleted = this.answers.stream()
			.filter(answer -> !answer.isDeleted())
			.collect(Collectors.toList());
		return answersNotDeleted;
	}

	public boolean contains(Answer answer) {
		return answers.contains(answer);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Answers answers1 = (Answers)o;
		return Objects.equals(answers, answers1.answers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(answers);
	}
}
