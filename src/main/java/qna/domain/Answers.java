package qna.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question")
	private final Set<Answer> answerSet;

	public Answers() {
		this.answerSet = new HashSet<>();
	}

	private Answers(Collection<Answer> answers) {
		this.answerSet = new HashSet<>(answers);
	}

	public static Answers of(Collection<Answer> answers) {
		return new Answers(answers);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Answers answers1 = (Answers)o;

		return Objects.equals(answerSet, answers1.answerSet);
	}

	@Override
	public int hashCode() {
		return answerSet.hashCode();
	}

	public boolean containsAllSameWriter(User user) {
		return answerSet.stream()
			.map(Answer::getWriter)
			.allMatch(writer -> writer.equals(user));
	}

	public Set<DeleteHistory> deleteAll() {
		return answerSet.stream()
			.map(Answer::delete).collect(Collectors.toSet());
	}

	public Answers addAll(Collection<Answer> answers) {
		answerSet.addAll(answers);
		return this;
	}

	public Answers add(Answer answer) {
		this.answerSet.add(answer);
		return this;
	}
}
