package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class AnswerGroup {

	@OneToMany(mappedBy = "question", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private List<Answer> answers = new ArrayList<>();

	protected AnswerGroup() {
	}

	public static AnswerGroup generate() {
		return new AnswerGroup();
	}

	public int size() {
		return answers.size();
	}

	public void add(Answer answer) {
		answers.add(answer);
	}

	public boolean containsAll(AnswerGroup answerGroup) {
		return answers.containsAll(answerGroup.answers);
	}

	public boolean isEmpty() {
		return answers.isEmpty();
	}

	public void checkAnswersAreSameWithUserAndWriter(User loginUser) {
		answers.forEach(answer -> answer.checkIsOwner(loginUser));
	}

	public List<DeleteHistory> deleteAll() {
		return answers.stream()
			.map(Answer::delete)
			.collect(Collectors.toList());
	}

	public List<Answer> answers() {
		return answers;
	}
}
