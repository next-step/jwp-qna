package qna.domain;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question")
	private final List<Answer> answers;

	protected Answers() {
		this(new ArrayList<>());
	}

	private Answers(Collection<Answer> answers) {
		this.answers = new ArrayList<>(answers);
	}

	public static Answers of(Collection<Answer> answers) {
		return new Answers(answers);
	}

	public static Answers of() {
		return new Answers();
	}

	private boolean isAllSameWriter(User user) {
		return answers.stream()
			.allMatch(answer -> answer.isOwner(user));
	}

	public DeleteHistories deleteAll(User user) throws CannotDeleteException {
		validateDeleteAll(user);
		return answers.stream()
			.map(Answer::delete)
			.collect(Collectors.collectingAndThen(toList(), DeleteHistories::of));
	}

	private void validateDeleteAll(User user) throws CannotDeleteException {
		if (!isAllSameWriter(user)) {
			throw new CannotDeleteException(
				ErrorCode.DELETE_QUESTION_OTHER_WRITER_ANSWER.getMessage());
		}
	}

	public Answers addAll(Collection<Answer> answers) {
		this.answers.addAll(answers);
		return this;
	}

	public Answers add(Answer answer) {
		this.answers.add(answer);
		return this;
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
		return answers.hashCode();
	}

}
