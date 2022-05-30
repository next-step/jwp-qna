package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {
	@OneToMany(mappedBy = "question")
	private List<Answer> answers = new ArrayList<>();

	protected Answers() {
		answers = new ArrayList<>();
	}

	public Answers(List<Answer> answers) {
		this.answers = answers;
	}

	public void add(Answer answer) {
		answers.add(answer);
	}
}
