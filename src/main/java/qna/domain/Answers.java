package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {
	@OneToMany(mappedBy = "question")
	private List<Answer> answers;

	public Answers(List<Answer> answers) {
		this.answers = answers;
	}

	protected Answers() {
		this.answers = new ArrayList<>();
	}

	public boolean haveNotOwner(User loginUser) {
		return answers.stream()
			.anyMatch(answer -> !answer.isOwner(loginUser));
	}

	public List<Answer> get() {
		return answers;
	}

	public void setDeleted(boolean isDelete) {
		for (Answer answer: answers) {
			answer.setDeleted(isDelete);
		}
	}

	public DeleteHistories delete(User loginUser) throws CannotDeleteException {
		DeleteHistories deleteHistories = new DeleteHistories();
		for (Answer answer: answers) {
			answer.delete(loginUser);
		}
		deleteHistories.add(this);
		return deleteHistories;
	}

	public void add(Answer answer) {
		answers.add(answer);
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
