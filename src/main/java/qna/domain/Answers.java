package qna.domain;

import static java.util.stream.Collectors.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers implements Serializable {

	@OneToMany(fetch = LAZY, mappedBy = "question", cascade = ALL)
	private List<Answer> answers = new ArrayList<>();

	public Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public Answers(Answer... answers) {
		this.answers = Arrays.stream(answers).collect(toList());
	}

	public Answers() {
	}

	public void addAnswer(Answer answer) {
		this.answers.add(answer);
	}

	public void addAnswers(List<Answer> answers, Question question) {
		this.answers = answers.stream()
			.peek(answer -> answer.toQuestion(question))
			.collect(toList());
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
