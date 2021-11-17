package qna.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question")
	private final List<Answer> answerList;

	protected Answers() {
		this.answerList = new ArrayList<>();
	}

	private Answers(Collection<Answer> answers) {
		this.answerList = new ArrayList<>(answers);
	}

	public static Answers of(Collection<Answer> answers) {
		return new Answers(answers);
	}

	public static Answers of() {
		return new Answers();
	}

	public boolean isAllSameWriter(User user) {
		return answerList.stream()
			.allMatch(answer -> answer.isOwner(user));
	}

	public List<DeleteHistory> deleteAll() {
		return answerList.stream()
			.map(Answer::delete).collect(Collectors.toList());
	}

	public Answers addAll(Collection<Answer> answers) {
		answerList.addAll(answers);
		return this;
	}

	public Answers add(Answer answer) {
		this.answerList.add(answer);
		return this;
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

}
