package qna.domain;

import static javax.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Where;

@Embeddable
public class Answers {

	@OneToMany(fetch = LAZY, mappedBy = "question")
	@Where(clause = "deleted = false")
	private List<Answer> answers = new ArrayList<>();

	protected Answers() { }

	private Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public static Object of(Answer... answers) {
		return new Answers(Arrays.asList(answers));
	}

	DeleteHistories delete(User user, LocalDateTime deletedAt) {
		List<DeleteHistory> deleteHistories = answers.stream()
			.map(answer -> answer.delete(user, deletedAt))
			.collect(Collectors.toList());
		return DeleteHistories.of(deleteHistories);
	}

	void add(Answer answer) {
		answers.add(answer);
	}

	void remove(Answer answer) {
		answers.remove(answer);
	}

	boolean isDeleted() {
		return this.answers
			.stream()
			.allMatch(Answer::isDeleted);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Answers other = (Answers)o;
		return Objects.equals(answers, other.answers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(answers);
	}
}
