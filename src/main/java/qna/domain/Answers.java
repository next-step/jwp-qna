package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
	private List<Answer> answers = new ArrayList<>();

	public Answers() {
	}

	public void add(Answer answer) {
		this.answers.add(answer);
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}
}
