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
	private List<Answer> answers = new ArrayList<>();

	public void add(Answer answer) {
		answers.add(answer);
	}

	public int size() {
		return answers.size();
	}

	public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
		return answers.stream()
			.map(answer -> answer.delete(loginUser))
			.collect(Collectors.toList());
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
