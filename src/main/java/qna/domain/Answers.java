package qna.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question")
	private final List<Answer> answerList;

	public Answers() {
		this.answerList = new ArrayList<>();
	}

	private Answers(Collection<Answer> answers) {
		this.answerList = new ArrayList<>(answers);
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

		return Objects.equals(answerList, answers1.answerList);
	}

	@Override
	public int hashCode() {
		return answerList.hashCode();
	}

	public boolean containsAllSameWriter(User user) {
		return answerList.stream()
			.map(Answer::getWriter)
			.allMatch(writer -> writer.equals(user));
	}

	public Set<DeleteHistory> deleteAll() {
		return answerList.stream()
			.map(Answer::delete).collect(Collectors.toSet());
	}

	public Answers addAll(Collection<Answer> answers) {
		answerList.addAll(answers);
		return this;
	}

	public Answers add(Answer answer) {
		this.answerList.add(answer);
		return this;
	}
}
