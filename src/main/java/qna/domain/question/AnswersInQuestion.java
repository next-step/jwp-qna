package qna.domain.question;

import qna.domain.answer.Answer;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class AnswersInQuestion {

	@OneToMany(mappedBy = "question")
	private List<Answer> answers = new ArrayList<>();

	public boolean add(Answer answer) {
		if(this.answers.contains(answer)) {
			return false;
		}

		return this.answers.add(answer);
	}

	public List<Answer> getAnswers() {
		return Collections.unmodifiableList(this.answers);
	}
}
