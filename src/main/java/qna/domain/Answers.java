package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {
	@OneToMany(mappedBy = "question")
	private List<Answer> answerList = new ArrayList<>();

	public void add(Answer answer) {
		answerList.add(answer);
	}
}
