package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	public boolean contains(Answer answer) {
		return this.answers.contains(answer);
	}

	public void deleteAnswer(){
		this.answers.stream()
			.forEach(answer -> answer.setDeleted(true));
	}

	public boolean isAnswersByUser(User loginUser) {
		return this.answers.stream()
			.allMatch(answer -> answer.isOwner(loginUser));
	}

	public List<DeleteHistory> makeDeleteHistories(User loginUser) {
		return this.answers.stream()
			.map(answer -> new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser))
			.collect(Collectors.toList());
	}
}
