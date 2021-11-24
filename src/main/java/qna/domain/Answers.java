package qna.domain;

import java.util.List;
import java.util.Objects;

public class Answers {
	private final List<Answer> answers;

	public Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public boolean haveNotOwner(User loginUser) {
		for (Answer answer : answers) {
			if (!answer.isOwner(loginUser)) {
				return true;
			}
		}
		return false;
	}

	public List<Answer> getAnswers() {
		return answers;
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
