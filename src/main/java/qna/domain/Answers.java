package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Answer> answers = new ArrayList<>();

	public void add(Answer answer) {
		answers.add(answer);
	}

	public int size() {
		return answers.size();
	}

	public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
		final List<DeleteHistory> deleteHistories = new ArrayList<>();
		for (Answer answer : answers) {
			deleteHistories.add(answer.delete(loginUser));
		}
		return deleteHistories;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Answers))
			return false;
		Answers answers1 = (Answers)o;
		return Objects.equals(answers, answers1.answers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(answers);
	}
}
